package com.sppart.admin.user.jwt.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    private String id;
    private String refreshToken;

    @Builder
    public RefreshToken(String id, String refreshToken) {
        this.id = id;
        this.refreshToken = refreshToken;
    }

    public boolean isSameId(String id) {
        return this.id.equals(id);
    }
}
