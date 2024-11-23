package com.example.finalproject.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
    // ... constructor and getter ...
}
