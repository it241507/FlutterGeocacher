package at.ac.fhstp.awp_bad.groupxx.repository;

import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.Comment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByCacheId(Long cacheId);
}
