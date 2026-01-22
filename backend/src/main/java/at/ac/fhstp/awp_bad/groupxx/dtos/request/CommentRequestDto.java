package at.ac.fhstp.awp_bad.groupxx.dtos.request;

import java.time.Instant;

public class CommentRequestDto {
    private String message;
    private Long cacheId;

// Getter and Setter


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCacheId() {
        return cacheId;
    }

    public void setCacheId(Long cacheId) {
        this.cacheId = cacheId;
    }
}
