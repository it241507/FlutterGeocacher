package at.ac.fhstp.awp_bad.groupxx.service;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.UserRegisterDto;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.dtos.request.UserLoginDto;
import at.ac.fhstp.awp_bad.groupxx.service.exceptions.UserAlreadyExistsException;
import org.springframework.security.authentication.BadCredentialsException;

public interface UserService {

    User login(UserLoginDto userLoginDto) throws BadCredentialsException;
    User createUser(UserRegisterDto userRegisterDto) throws UserAlreadyExistsException;
}
