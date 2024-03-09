package com.sppart.admin.user.service.token;

import com.sppart.admin.user.jwt.dto.RefreshToken;

public interface TokenService {
    public void setTokenValue(RefreshToken refreshToken);

    public <T> T getTokenValue(String key, Class<T> classType);

    public void deleteTokenValue(String key);
}
