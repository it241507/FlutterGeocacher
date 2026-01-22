package at.ac.fhstp.awp_bad.groupxx.repository;

import at.ac.fhstp.awp_bad.groupxx.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByMailIgnoreCase(String mail);

}
