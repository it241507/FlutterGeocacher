package at.ac.fhstp.awp_bad.groupxx.service;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.CommentRequestDto;
import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.Comment;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.repository.CacheRepository;
import at.ac.fhstp.awp_bad.groupxx.repository.CommentRepository;
import at.ac.fhstp.awp_bad.groupxx.repository.UserRepository;
import at.ac.fhstp.awp_bad.groupxx.util.Mapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CacheRepository cacheRepository;
   // private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final Mapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, CacheRepository cacheRepository/*, UserRepository userRepository*/, Mapper mapper) {
        this.cacheRepository = cacheRepository;
        this.commentRepository = commentRepository;
       // this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public Comment save(CommentRequestDto commentRequestDto, User user) throws IOException {
        Optional<Cache> optionalCache = cacheRepository.findById(commentRequestDto.getCacheId());
        Comment comment = mapper.commentRequestDtoToComment(commentRequestDto);
        comment.setUser(user);
        comment.setTimeStamp(Instant.now());
        optionalCache.ifPresent(comment::setCache);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    public List<Comment> getCommentsByCacheId(Long cacheId) {
        return commentRepository.findByCacheId(cacheId);
    }
}
