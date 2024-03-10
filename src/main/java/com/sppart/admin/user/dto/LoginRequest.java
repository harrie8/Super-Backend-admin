package com.sppart.admin.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    private String id;
    private String password;

    @Builder
    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
