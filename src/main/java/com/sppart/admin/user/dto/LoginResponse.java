package com.sppart.admin.user.dto;

import com.sppart.admin.user.domain.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Users user;
    private final String message;

    @Builder
    public LoginResponse(Users user, String message) {
        this.user = user;
        this.message = message;
    }
}
