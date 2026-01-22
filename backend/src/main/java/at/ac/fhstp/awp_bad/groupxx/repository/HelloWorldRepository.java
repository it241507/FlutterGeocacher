package at.ac.fhstp.awp_bad.groupxx.repository;

import at.ac.fhstp.awp_bad.groupxx.entities.HelloWorldMessage;
import org.springframework.data.repository.CrudRepository;

public interface HelloWorldRepository extends CrudRepository<HelloWorldMessage, Long> {}

