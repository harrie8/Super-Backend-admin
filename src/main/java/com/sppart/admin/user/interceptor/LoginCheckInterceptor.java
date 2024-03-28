package com.sppart.admin.user.interceptor;

import com.sppart.admin.exception.CommonErrorCode;
import com.sppart.admin.exception.SuperpositionAdminException;
import com.sppart.admin.user.domain.Users;
import com.sppart.admin.utils.SessionConst;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Order(1)
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    // todo 예외 처리

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (shouldNotFilter(request)) {
            return true;
        }

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (isAuthNotRequired(auth)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Users loginUser = (Users) session.getAttribute(SessionConst.LOGIN_USER);
        if (loginUser == null) {
            return false;
        }

        checkRole(auth, loginUser);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private boolean shouldNotFilter(HttpServletRequest request) {
        String[] excludePath = {"/users/signup", "/users/login"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    private boolean isAuthNotRequired(Auth auth) {
        return auth == null;
    }

    private void checkRole(Auth auth, Users loginUser) {
        Arrays.stream(auth.roles())
                .filter(role -> role.equals(loginUser.getRole()))
                .findAny()
                .orElseThrow(() -> new SuperpositionAdminException(CommonErrorCode.FORBIDDEN));
    }
}
