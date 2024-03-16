package com.sppart.admin.user.service.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sppart.admin.user.exception.InvalidTokenException;
import com.sppart.admin.user.jwt.dto.RefreshToken;
import com.sppart.admin.utils.JwtUtils;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RedisTokenService implements TokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void setTokenValue(RefreshToken refreshToken) {
        String key = refreshToken.getRefreshToken();
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(refreshToken));
            redisTemplate.expire(key, JwtUtils.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS); //TTL 7 days
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T getTokenValue(String key, Class<T> classType) {
        String redisValue = (String) redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasText(redisValue)) {
            throw new InvalidTokenException();
        }

        try {
            return objectMapper.readValue(redisValue, classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTokenValue(String key) {
        redisTemplate.delete(key);
    }
}
