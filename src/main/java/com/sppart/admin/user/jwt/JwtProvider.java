package com.sppart.admin.user.jwt;

import com.sppart.admin.user.jwt.dto.CreateTokenDto;
import com.sppart.admin.user.jwt.dto.JwtToken;
import com.sppart.admin.user.jwt.dto.RefreshToken;
import com.sppart.admin.user.service.token.TokenService;
import com.sppart.admin.utils.Authority;
import com.sppart.admin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private final Key key;
    private final TokenService tokenService;

    public JwtProvider(@Value("${spring.security.jwt.token.secret-key}") String secretKey, TokenService tokenService) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.tokenService = tokenService;
    }

    public JwtToken generateJwtToken(CreateTokenDto dto) {
        Date now = new Date();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now.getTime() + JwtUtils.ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(dto.getId()) //payload "sub" : "id"
                .claim(JwtUtils.AUTHORITY, dto.getAuthority()) //payload "authority" : "ROLE_ADMIN" or "ROLE_MANAGER"
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiresIn) //payload "exp" : 2 hours
                .signWith(key, SignatureAlgorithm.HS512) //header "alg" : 해싱 알고리즘 HS512
                .compact();

        return JwtToken.builder()
                .id(dto.getId())
                .grantType(JwtUtils.BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(generateRefreshToken(dto.getId(), now))
                .build();
    }

    private RefreshToken generateRefreshToken(String id, Date now) {
        long refreshTokenExpireIn = (now.getTime()) + JwtUtils.REFRESH_TOKEN_EXPIRE_TIME;

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(refreshTokenExpireIn))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        RefreshToken token = RefreshToken.builder()
                .id(id)
                .refreshToken(refreshToken)
                .build();

        tokenService.setTokenValue(token);

        return token;
    }

    private Authority authorityFromClaims(Claims claims) {
        String roleName = claims.get(JwtUtils.AUTHORITY).toString();
        return Authority.findByName(roleName);
    }

    public boolean validateToken(String token) throws RuntimeException {
        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return true;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
