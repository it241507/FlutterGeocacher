package at.ac.fhstp.awp_bad.groupxx.rest;

import at.ac.fhstp.awp_bad.groupxx.auth.TokenBlacklist;
import at.ac.fhstp.awp_bad.groupxx.auth.TokenUtil;
import at.ac.fhstp.awp_bad.groupxx.dtos.request.UserLoginDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.request.UserRegisterDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.AuthTokenDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.CacheResponseDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.UserResponseDto;
import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.service.CacheService;
import at.ac.fhstp.awp_bad.groupxx.service.UserService;
import at.ac.fhstp.awp_bad.groupxx.service.exceptions.UserAlreadyExistsException;
import at.ac.fhstp.awp_bad.groupxx.util.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/")
public class UserEndpoint {

    private final UserService userService;
    private final TokenUtil tokenUtil;
    private final CacheService cacheService;
    private final Mapper mapper;
    private final TokenBlacklist tokenBlacklist;

    public UserEndpoint(UserService userService, TokenUtil tokenUtil, TokenBlacklist tokenBlacklist, CacheService cacheService, Mapper mapper) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
        this.cacheService = cacheService;
        this.mapper = mapper;
        this.tokenBlacklist = tokenBlacklist;
    }

    @PostMapping("/login")
    public AuthTokenDto login(@RequestBody UserLoginDto userLoginDto){
        User user = null;
        try {
            user = userService.login(userLoginDto);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

        AuthTokenDto authTokenDto = new AuthTokenDto();
        authTokenDto.setAuthToken(tokenUtil.generateToken(user));
        return authTokenDto;
    }

    @PostMapping("/registration")
    public AuthTokenDto registerUser(@RequestBody UserRegisterDto userRegisterDto){
        User user = null;
        try {
            user = userService.createUser(userRegisterDto);
        } catch (UserAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }

        AuthTokenDto authTokenDto = new AuthTokenDto();
        authTokenDto.setAuthToken(tokenUtil.generateToken(user));
        return authTokenDto;
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.blacklistTokens(token);
        }
        return ResponseEntity.ok("Logged out successfully.");
    }

    @GetMapping("/{userId}")
    public UserResponseDto getUserById(@PathVariable String userId) {
        Optional<User> optionalUser = cacheService.findUserById(userId);
        if (optionalUser.isPresent()) {
            return mapper.userToUserResponseDto(optionalUser.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }

    @GetMapping("/{userId}/caches")
    public List<CacheResponseDto> getCachesByUser(@PathVariable String userId) {
        Optional<User> optionalUser = cacheService.findUserById(userId);
        if (optionalUser.isPresent()) {
            List<Cache> caches = cacheService.findCachesByUser(optionalUser.get());
            return caches.stream()
                    .map(mapper::cache2CacheResponseDto)
                    .collect(Collectors.toList());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
    }

    @GetMapping("/current")
    public UserResponseDto getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Optional<User> optionalUser = tokenUtil.parseToken(token);
            if (optionalUser.isPresent()) {
                return mapper.userToUserResponseDto(optionalUser.get());
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token!");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authorization header is missing or invalid!");
        }
    }
}
