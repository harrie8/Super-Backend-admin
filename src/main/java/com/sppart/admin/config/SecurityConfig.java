package com.sppart.admin.config;

import com.sppart.admin.user.jwt.JwtAccessDeniedHandler;
import com.sppart.admin.user.jwt.JwtAuthenticationEntryPoint;
import com.sppart.admin.user.jwt.JwtAuthenticationFilter;
import com.sppart.admin.user.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final JwtAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        //swagger-ui
        return (web) -> web.ignoring().antMatchers(
                "/v3/api-docs/**", "/configuration/ui",
                "/swagger-resources", "/configuration/security",
                "/swagger-ui/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .headers()
                .frameOptions()
                .sameOrigin()
                .and()
                .cors() // CORS 에러 방지용

                // 세션을 사용하지 않을거라 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 접근 권한 설정부
                .and().authorizeHttpRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()

                // JWT 토큰 예외처리부
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
