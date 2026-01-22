package at.ac.fhstp.awp_bad.groupxx.service;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.UserRegisterDto;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.dtos.request.UserLoginDto;
import at.ac.fhstp.awp_bad.groupxx.repository.UserRepository;
import at.ac.fhstp.awp_bad.groupxx.service.exceptions.UserAlreadyExistsException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.tags.ParamAware;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(UserLoginDto userLoginDto) throws BadCredentialsException {
        Optional<User> userOptional = userRepository.findByMailIgnoreCase(userLoginDto.getEmail());

        if (userOptional.isEmpty() || !passwordEncoder.matches(userLoginDto.getPassword(), userOptional.get().getPwhash())) {
            throw new BadCredentialsException("Invalid username/password!");
        }
        return userOptional.get();
    }

    @Override
    public User createUser(UserRegisterDto userRegisterDto) throws UserAlreadyExistsException {

        if(userRepository.findByMailIgnoreCase(userRegisterDto.getMail()).isPresent()){
            throw new UserAlreadyExistsException("User already exists!");
        }

        User user = new User();
        user.setName(userRegisterDto.getName());
        user.setMail(userRegisterDto.getMail());
        user.setPwhash(passwordEncoder.encode(userRegisterDto.getPw()));

        return userRepository.save(user);

    }
}
