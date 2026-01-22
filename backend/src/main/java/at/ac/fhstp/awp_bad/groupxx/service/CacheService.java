package at.ac.fhstp.awp_bad.groupxx.service;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.CacheRequestDto;
import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.service.exceptions.InvalidImageException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CacheService {

    Cache save(CacheRequestDto cacheRequestDto, User user) throws IOException, InvalidImageException;
    Optional<Cache> findById(Long id);
    byte[] loadImage(String fileName) throws InvalidImageException, IOException;


    List<Cache> getCachesInArea(Double north, Double east, Double south, Double west);

    Optional<User> findUserById(String userId);
    List<Cache> findCachesByUser(User user);
}
