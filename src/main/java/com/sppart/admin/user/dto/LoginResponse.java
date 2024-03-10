package com.sppart.admin.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final ResponseUserInfo userInfo;
    private final String accessToken;
    private final String message;

    @Builder
    public LoginResponse(ResponseUserInfo userInfo, String accessToken, String message) {
        this.userInfo = userInfo;
        this.accessToken = accessToken;
        this.message = message;
    }
}
