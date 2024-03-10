package com.sppart.admin.user.service;

import com.sppart.admin.user.domain.mapper.UserMapper;
import com.sppart.admin.user.dto.LoginRequest;
import com.sppart.admin.user.dto.LoginResponse;
import com.sppart.admin.user.dto.ResponseUserInfo;
import com.sppart.admin.user.exception.InvalidTokenException;
import com.sppart.admin.user.jwt.JwtProvider;
import com.sppart.admin.user.jwt.dto.AccessToken;
import com.sppart.admin.user.jwt.dto.CreateTokenDto;
import com.sppart.admin.user.jwt.dto.JwtToken;
import com.sppart.admin.user.jwt.dto.RefreshToken;
import com.sppart.admin.user.service.token.TokenService;
import com.sppart.admin.utils.CookieUtils;
import com.sppart.admin.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ResponseEntity<?> login(LoginRequest request) {
        ResponseUserInfo userInfoById = userMapper.getUserInfoById(request.getId());
        checkPassword(request.getPassword(), userInfoById.getPassword());

        CreateTokenDto createTokenDto = CreateTokenDto.builder()
                .id(userInfoById.getId())
                .authority(userInfoById.getAuthority())
                .build();

        JwtToken jwtToken = jwtProvider.generateJwtToken(createTokenDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, setCookie(jwtToken.getRefreshToken().getRefreshToken()))
                .body(LoginResponse.builder()
                        .userInfo(userInfoById)
                        .accessToken(jwtToken.getAccessToken())
                        .message("success").build());
    }

    private void checkPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public void logout(String rt) {
        if (StringUtils.hasText(rt)) {
            tokenService.deleteTokenValue(rt);
        } else {
            throw new IllegalArgumentException("401");
        }
    }

    @Override
    public ResponseEntity<?> regenerateToken(String rt) {
        if (!StringUtils.hasText(rt)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh Token is blank");
        }

        try {
            RefreshToken refreshToken = tokenService.getTokenValue(rt, RefreshToken.class);
            ResponseUserInfo userInfoById = userMapper.getUserInfoById(refreshToken.getId());
            CreateTokenDto createTokenDto = CreateTokenDto.builder()
                    .id(userInfoById.getId())
                    .authority(userInfoById.getAuthority())
                    .build();

            JwtToken jwtToken = jwtProvider.generateJwtToken(createTokenDto);

            return ResponseEntity.ok()
                    .body(AccessToken.builder().accessToken(jwtToken.getAccessToken()).build());
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Refresh Token is Expired");
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    private String setCookie(String refreshToken) {
        ResponseCookie cookie = ResponseCookie
                .from(CookieUtils.COOKIE_HEADER_NAME, refreshToken)
                .maxAge(JwtUtils.REFRESH_TOKEN_EXPIRE_TIME)
                .httpOnly(true)
                .secure(true)
                .sameSite(CookieUtils.COOKIE_SAME_SITE)
                .path(CookieUtils.COOKIE_PATH)
                .domain(CookieUtils.DOMAIN)
                .build();

        return cookie.toString();
    }

}
