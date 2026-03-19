package com.consultrix.consultrixserver.model.dto.authDTO;

public class LoginResponseDto {

    private String accessToken;
    private String tokenType;
    private long expiresInSeconds;
    private String role;

    public LoginResponseDto(String accessToken, long expiresInSeconds, String role) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.expiresInSeconds = expiresInSeconds;
        this.role = role;
    }

    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public long getExpiresInSeconds() { return expiresInSeconds; }
    public String getRole() { return role; }
}