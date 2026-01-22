package at.ac.fhstp.awp_bad.groupxx.util;

import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.Coordinate;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.repository.CacheRepository;
import at.ac.fhstp.awp_bad.groupxx.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Optional;

@Component
public class TestDataGenerator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CacheRepository cacheRepository;

    public TestDataGenerator(UserRepository userRepository, PasswordEncoder passwordEncoder, CacheRepository cacheRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cacheRepository = cacheRepository;
    }

    @PostConstruct
    private void saveCache(){
        generateUser();
        Optional<User> userOptional = userRepository.findByMailIgnoreCase("test@gmail.com");
        User user = null;
        if(userOptional.isPresent()) {
            user = userOptional.get();
        }

        Cache cache1 = new Cache();
        cache1.setUser(user);
        cache1.setDesc("Testcache 1 - FH Stp.");
        cache1.setTimeStamp(Instant.now());
        cache1.setTitle("TestCache");
        cache1.setCoordinate(new Coordinate(48.2136616, 15.628867));
        cache1.setImageFilename("44f75603-8b89-4439-a0b0-194ea4cdaf44.jpg");
        cacheRepository.save(cache1);
    }


    private void generateUser() {
        User user = new User();
        user.setName("Testuser");
        user.setMail("test@gmail.com");
        user.setPwhash(passwordEncoder.encode("testpw"));
        userRepository.save(user);
    }

}
