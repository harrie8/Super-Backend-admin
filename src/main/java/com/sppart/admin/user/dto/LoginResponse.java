package com.sppart.admin.user.dto;

import com.sppart.admin.user.domain.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Users users;
    private final String message;

    @Builder
    public LoginResponse(Users users, String message) {
        this.users = users;
        this.message = message;
    }
}
