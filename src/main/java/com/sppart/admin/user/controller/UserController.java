package com.sppart.admin.user.controller;

import com.sppart.admin.user.domain.Users;
import com.sppart.admin.user.dto.CurrentUser;
import com.sppart.admin.user.dto.LoginDto;
import com.sppart.admin.user.dto.LoginRequest;
import com.sppart.admin.user.dto.LoginResponse;
import com.sppart.admin.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                               HttpServletRequest httpServletRequest) {
        LoginDto loginDto = LoginDto.builder()
                .loginRequest(loginRequest)
                .httpServletRequest(httpServletRequest)
                .build();
        LoginResponse loginResponse = userService.login(loginDto);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@CurrentUser Users users, HttpServletRequest request) {
        userService.logout(request);
    }
}
