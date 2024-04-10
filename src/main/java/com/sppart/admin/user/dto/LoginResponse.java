package com.sppart.admin.user.dto;

import com.sppart.admin.user.domain.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final Users userInfo;
    private final String message;

    @Builder
    public LoginResponse(Users userInfo, String message) {
        this.userInfo = userInfo;
        this.message = message;
    }
}
