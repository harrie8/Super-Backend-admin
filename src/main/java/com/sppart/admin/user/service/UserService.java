package com.sppart.admin.user.service;

import com.sppart.admin.user.dto.LoginRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> login(LoginRequest request);

    void logout(String rt);

    ResponseEntity<?> regenerateToken(String refreshToken);
}
