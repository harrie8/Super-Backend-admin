package com.sppart.admin.config;

import com.sppart.admin.user.filter.CustomUsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/v3/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**");
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
                .cors()

                .and().authorizeHttpRequests()
                .antMatchers("/users/login").permitAll()
                .antMatchers("/users/logout").authenticated()
//                .antMatchers("/**").permitAll()
//                .anyRequest().authenticated();

                .and()
                .addFilterBefore(new CustomUsernamePasswordAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
