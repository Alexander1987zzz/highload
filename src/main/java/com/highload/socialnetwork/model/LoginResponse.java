package com.highload.socialnetwork.model;

public class LoginResponse {
    private final String token;
    private final String errorMessage;

    public LoginResponse(String token, String errorMessage) {
        this.token = token;
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
