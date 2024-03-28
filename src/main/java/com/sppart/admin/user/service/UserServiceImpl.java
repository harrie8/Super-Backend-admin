package com.sppart.admin.user.service;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.user.domain.Users;
import com.sppart.admin.user.domain.mapper.UserMapper;
import com.sppart.admin.user.dto.LoginDto;
import com.sppart.admin.user.dto.LoginResponse;
import com.sppart.admin.user.exception.UserErrorCode;
import com.sppart.admin.utils.SessionConst;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginResponse login(LoginDto dto) {
        Users userById = userMapper.getUserById(dto.getLoginId())
                .orElseThrow(() -> new SuperpositionAdminException(UserErrorCode.ID_OR_PW_NOT_VALID));
        checkPassword(dto.getLoginPassword(), userById.getPassword());

        HttpServletRequest httpServletRequest = dto.getHttpServletRequest();
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, userById);

        return LoginResponse.builder()
                .users(userById)
                .message("success")
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
    }

    private void checkPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new SuperpositionAdminException(UserErrorCode.ID_OR_PW_NOT_VALID);
        }
    }
}
