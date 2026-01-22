package at.ac.fhstp.awp_bad.groupxx.service;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.CommentRequestDto;
import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.Comment;
import at.ac.fhstp.awp_bad.groupxx.entities.User;

import java.io.IOException;
import java.util.List;

public interface CommentService {

    Comment save(CommentRequestDto commentRequestDto, User user) throws IOException;

    List<Comment> getCommentsByCacheId(Long cacheId);

}
