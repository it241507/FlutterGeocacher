package at.ac.fhstp.awp_bad.groupxx.rest;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.CacheRequestDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.CacheResponseDto;
import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.Comment;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.service.CacheService;
import at.ac.fhstp.awp_bad.groupxx.service.CommentService;
import at.ac.fhstp.awp_bad.groupxx.service.exceptions.InvalidImageException;
import at.ac.fhstp.awp_bad.groupxx.util.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/api/caches")
public class CacheEndpoint {
    private final CacheService cacheService;
    private final Mapper mapper;
    private final CommentService commentService;

    public CacheEndpoint(CacheService cacheService,
                         Mapper mapper, CommentService commentService) {
        this.cacheService = cacheService;
        this.mapper = mapper;
        this.commentService = commentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CacheResponseDto postCache(@RequestBody CacheRequestDto cacheRequestDto, Authentication authentication) throws
            IOException {
        try {
            return mapper.cache2CacheResponseDto(cacheService.save(cacheRequestDto, (User) authentication.getPrincipal()));
        } catch (InvalidImageException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public CacheResponseDto getCacheById(@PathVariable("id") Long id){
        Optional<Cache> optionalCache = cacheService.findById(id);
        if(optionalCache.isPresent()){
            return mapper.cache2CacheResponseDto(optionalCache.get());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cache not found!");
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename) throws IOException {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(MediaType.IMAGE_JPEG_VALUE))
                    .body(cacheService.loadImage(filename));
        } catch (InvalidImageException e) {
            throw new  ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/map/{north}/{east}/{south}/{west}")
    public List<CacheResponseDto> getCachesInArea(@PathVariable Double north,
                                                  @PathVariable Double east,
                                                  @PathVariable Double south,
                                                  @PathVariable Double west){
        List<Cache> results = cacheService.getCachesInArea(north, east, south, west);
        List<CacheResponseDto> cacheResponses = new ArrayList<>();



        for (Cache cache : results) {

            cacheResponses.add(mapper.cache2CacheResponseDto(cache));
        }
        return cacheResponses;
    }

}
