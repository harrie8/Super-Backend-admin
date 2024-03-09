package com.sppart.admin.user.jwt.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessToken {
    private String accessToken;
}
