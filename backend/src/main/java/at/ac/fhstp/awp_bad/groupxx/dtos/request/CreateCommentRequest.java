package at.ac.fhstp.awp_bad.groupxx.dtos.request;

public class CreateCommentRequest {

    private String text;
    private Long userId;
    private Long cacheId;

    // Getter and Setter


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCacheId() {
        return cacheId;
    }

    public void setCacheId(Long cacheId) {
        this.cacheId = cacheId;
    }
}
