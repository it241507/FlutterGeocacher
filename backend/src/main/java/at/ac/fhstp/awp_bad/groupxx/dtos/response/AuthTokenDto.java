package at.ac.fhstp.awp_bad.groupxx.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthTokenDto {

    @JsonProperty("auth-token")
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
