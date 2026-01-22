package at.ac.fhstp.awp_bad.groupxx.util;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.CacheRequestDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.request.CommentRequestDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.CacheResponseDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.CommentResponseDto;
import at.ac.fhstp.awp_bad.groupxx.dtos.response.UserResponseDto;
import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.Comment;
import at.ac.fhstp.awp_bad.groupxx.entities.Coordinate;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class Mapper {

    public Cache cacheRequestDto2Cache(CacheRequestDto cacheRequestDto,
                                       Optional<String> filename) {
        Cache cache = new Cache();
        Coordinate coordinate = new Coordinate();
        coordinate.setLat(cacheRequestDto.getLat());
        coordinate.setLng(cacheRequestDto.getLng());
        cache.setCoordinate(coordinate);
        cache.setTitle(cacheRequestDto.getTitle());
        cache.setDesc(cacheRequestDto.getDesc());
        filename.ifPresent(cache::setImageFilename);
        return cache;

    }

    public CacheResponseDto cache2CacheResponseDto(Cache cache) {
        CacheResponseDto cacheResponseDto = new CacheResponseDto();
        cacheResponseDto.setDesc(cache.getDesc());
        cacheResponseDto.setLat(cache.getCoordinate().getLat());
        cacheResponseDto.setLng(cache.getCoordinate().getLng());
        cacheResponseDto.setId(cache.getId());
        cacheResponseDto.setTitle(cache.getTitle());
        cacheResponseDto.setTimeStamp(cache.getTimeStamp());
        cacheResponseDto.setImageFilename(cache.getImageFilename());
        cacheResponseDto.setUserName(cache.getUser().getName());
        cacheResponseDto.setUserId(cache.getUser().getId()+"");
        cacheResponseDto.setComments(cache.getComments());
        return cacheResponseDto;

    }

    public UserResponseDto userToUserResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getMail());
        return dto;
    }

    public Comment commentRequestDtoToComment(CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setMessage(commentRequestDto.getMessage());
        //comment.se(commentRequestDto.getCacheId());
        return comment;
    }


    public CommentResponseDto commentToCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();

        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setTimeStamp(comment.getTimeStamp());
        dto.setUserId(comment.getUser().getId());
        dto.setUserName(comment.getUser().getName());
        dto.setCacheId(comment.getCache().getId());
        dto.setUser(comment.getUser());
        return dto;
    }


}
