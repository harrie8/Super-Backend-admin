package com.sppart.admin.user.service;

import com.sppart.admin.user.dto.LoginDto;
import com.sppart.admin.user.dto.LoginResponse;
import javax.servlet.http.HttpServletRequest;

public interface UserService {

    LoginResponse login(LoginDto loginDto);

    void logout(HttpServletRequest request);
}
