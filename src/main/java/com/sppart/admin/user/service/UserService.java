package com.sppart.admin.user.service;

import com.sppart.admin.user.dto.LoginDto;
import com.sppart.admin.user.dto.LoginResponse;
import com.sppart.admin.user.dto.LogoutDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    LoginResponse login(LoginDto loginDto);

    void logout(LogoutDto logoutDto);

    ResponseEntity<?> regenerateToken(String refreshToken);
}
