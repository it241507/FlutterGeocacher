package at.ac.fhstp.awp_bad.groupxx.service;

import at.ac.fhstp.awp_bad.groupxx.dtos.request.CacheRequestDto;
import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import at.ac.fhstp.awp_bad.groupxx.repository.CacheRepository;
import at.ac.fhstp.awp_bad.groupxx.repository.CommentRepository;
import at.ac.fhstp.awp_bad.groupxx.repository.UserRepository;
import at.ac.fhstp.awp_bad.groupxx.service.exceptions.InvalidImageException;
import at.ac.fhstp.awp_bad.groupxx.util.Mapper;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CacheServiceIpml implements CacheService {
    private final CacheRepository cacheRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final Mapper mapper;

    private static final Integer MAX_JPEG_SIZE = 512000;
    private static final String CACHE_IMAGE_TYPE = "image/jpeg";

    @Value("${uploadDir}")
    private String uploadDir;

    public CacheServiceIpml(CacheRepository cacheRepository, UserRepository userRepository, CommentRepository commentRepository, Mapper mapper) {
        this.cacheRepository = cacheRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    @Override
    public Cache save(CacheRequestDto cacheRequestDto, User user) throws IOException, InvalidImageException {
        String filename = null;
        if (cacheRequestDto.getImageBase64() != null) {
            filename = UUID.randomUUID().toString() + ".jpg";
            byte[] imageByte = Base64.getDecoder().decode(cacheRequestDto.getImageBase64());
            String contentType = new Tika().detect(imageByte);
            if (!contentType.equals(CACHE_IMAGE_TYPE)) {
                throw new InvalidImageException("Image is not a jpeg file!");
            }
            if (imageByte.length > MAX_JPEG_SIZE) {
                throw new InvalidImageException("Upload is to big!");
            }
            Path uploadPath = Path.of(uploadDir);
            Path filePath = uploadPath.resolve(filename);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.write(filePath, imageByte);
        }
        Cache cache = mapper.cacheRequestDto2Cache(cacheRequestDto, Optional.ofNullable(filename));
        cache.setTimeStamp(Instant.now());
        cache.setUser(user);
        return cacheRepository.save(cache);

    }

    @Override
    public Optional<Cache> findById(Long id) {
        return cacheRepository.findById(id);
    }
    @Override
    public byte[] loadImage(String fileName) throws InvalidImageException, IOException {
        Path uploadPath = Path.of(uploadDir);
        Path filePath = uploadPath.resolve(fileName);

        byte[] data = null;
        try{
            data = Files.readAllBytes(filePath);
        } catch (NoSuchFileException e){
            filePath = uploadPath.resolve("standard.jpg");
            data = Files.readAllBytes(filePath);
            //throw new InvalidImageException("Image not found!");
        }
        return data;
    }

    @Override
    public List<Cache> getCachesInArea(Double north, Double east, Double south, Double west) {
        return cacheRepository.getCachesInArea(north, east, south, west);
    }

    @Override
    public Optional<User> findUserById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<Cache> findCachesByUser(User user) {
        return cacheRepository.findByUser(user);
    }


}
