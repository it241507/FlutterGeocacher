package at.ac.fhstp.awp_bad.groupxx.rest;
import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.Comment;
import org.springframework.security.core.Authentication;
import at.ac.fhstp.awp_bad.groupxx.dtos.request.CommentRequestDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.CommentResponseDto;
import at.ac.fhstp.awp_bad.groupxx.service.CacheService;
import at.ac.fhstp.awp_bad.groupxx.service.CommentService;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.util.Mapper;
import io.jsonwebtoken.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentEndpoint {
    private final CommentService commentService;
    private final Mapper mapper;
    private final CacheService cacheService;

    public CommentEndpoint(CommentService commentService, Mapper mapper, CacheService cacheService) {
        this.commentService = commentService;
        this.mapper = mapper;
        this.cacheService = cacheService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto postComment(@RequestBody CommentRequestDto commentRequestDto, Authentication authentication) throws
            IOException  {
        try {
            return mapper.commentToCommentResponseDto(commentService.save(commentRequestDto, (User) authentication.getPrincipal()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @GetMapping("/cache/{cacheId}")
    public List<CommentResponseDto> getCommentsByCacheId(@PathVariable Long cacheId) {
        List<Comment> results = commentService.getCommentsByCacheId(cacheId);
        List<CommentResponseDto> commentResponses = new ArrayList<>();


        for (Comment comment : results) {
            commentResponses.add(mapper.commentToCommentResponseDto(comment));
        }
        return commentResponses;
    }

}
