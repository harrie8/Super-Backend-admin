package com.sppart.admin.user.jwt;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {
    private final Key key;
    private final TokenService tokenService;

    public JwtProvider(@Value("${spring.security.jwt.token.secret-key}") String secretKey, TokenService tokenService) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.tokenService = tokenService;
    }

    public JwtToken generateJwtToken(String email) {
        Date now = new Date();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now.getTime() + JwtUtils.ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(email) //payload "sub" : "email"
                .claim(JwtUtils.AUTHORIZATION_HEADER, Authority.ROLE_USER) //payload "auth" : "ROLE_USER"
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiresIn) //payload "exp" : 2 hours
                .signWith(key, SignatureAlgorithm.HS512) //header "alg" : 해싱 알고리즘 HS512
                .compact();

        return JwtToken.builder()
                .email(email)
                .grantType(JwtUtils.BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(generateRefreshToken(email, now))
                .build();
    }

    private RefreshToken generateRefreshToken(String email, Date now) {
        long refreshTokenExpireIn = (now.getTime()) + JwtUtils.REFRESH_TOKEN_EXPIRE_TIME;

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(refreshTokenExpireIn))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        RefreshToken token = RefreshToken.builder()
                .email(email)
                .refreshToken(refreshToken)
                .build();

        tokenService.setTokenValue(token);

        return token;
    }

    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        if (claims.get(JwtUtils.AUTHORIZATION_HEADER) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtUtils.AUTHORIZATION_HEADER).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
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
