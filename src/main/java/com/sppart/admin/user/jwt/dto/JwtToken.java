package com.sppart.admin.user.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtToken {
    private String email;
    private String grantType; // Bearer
    private String accessToken;
    private RefreshToken refreshToken;
}
