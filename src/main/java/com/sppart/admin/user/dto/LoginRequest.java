package com.sppart.admin.user.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    private String id;
    @NotBlank
    private String password;

    @Builder
    public LoginRequest(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
