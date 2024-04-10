package com.sppart.admin.user.service;

import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.user.domain.Accessor;
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
        Users findUser = userMapper.findById(dto.getLoginId())
                .orElseThrow(() -> new SuperpositionAdminException(UserErrorCode.ID_OR_PW_NOT_VALID));
        validPassword(dto.getLoginPassword(), findUser.getPassword());

        HttpServletRequest httpServletRequest = dto.getHttpServletRequest();
        HttpSession session = httpServletRequest.getSession();
        Accessor accessor = Accessor.builder()
                .id(findUser.getId())
                .role(findUser.getRole())
                .build();
        session.setAttribute(SessionConst.LOGIN_USER, accessor);
        session.setMaxInactiveInterval(SessionConst.DEFAULT_TIME_OUT_SECONDS);

        return LoginResponse.builder()
                .userInfo(findUser)
                .message("success")
                .build();
    }

    @Override
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
    }

    private void validPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new SuperpositionAdminException(UserErrorCode.ID_OR_PW_NOT_VALID);
        }
    }
}
