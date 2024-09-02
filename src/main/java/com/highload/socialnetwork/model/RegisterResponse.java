package com.highload.socialnetwork.model;

public class RegisterResponse {
    private final String message;
    private final User user;

    public RegisterResponse(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}
