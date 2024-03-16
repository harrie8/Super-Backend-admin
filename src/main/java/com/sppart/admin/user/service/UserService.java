package com.sppart.admin.user.service;

import com.sppart.admin.user.dto.LoginRequest;
import com.sppart.admin.user.dto.LogoutDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> login(LoginRequest request);

    void logout(LogoutDto logoutDto);

    ResponseEntity<?> regenerateToken(String refreshToken);
}
