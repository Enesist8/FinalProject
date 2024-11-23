package com.example.finalproject.model;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    USER, ADMIN, ORDER_MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
