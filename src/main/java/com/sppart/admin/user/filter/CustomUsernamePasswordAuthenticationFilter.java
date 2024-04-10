package com.sppart.admin.user.filter;

import com.sppart.admin.user.domain.Accessor;
import com.sppart.admin.utils.SessionConst;
import com.sppart.admin.utils.SessionErrorCode;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUsernamePasswordAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludePath = {"/users/login", "/swagger-ui", "/v3/api-docs", "/swagger-resources"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (isEmptySession(session)) {
            log.error("세션이 비어 있는 오류");
            sendErrorResponse(response, SessionErrorCode.EMPTY_SESSION.getHttpStatus().value(),
                    SessionErrorCode.EMPTY_SESSION.getMessage());
            return;
        }
        Object sessionAttribute = getSessionAttribute(session);
        if (!(sessionAttribute instanceof Accessor accessor)) {
            Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("JSESSIONID"))
                    .findFirst()
                    .ifPresent(cookie -> log.info("cookie.getName() = {}, cookie.getValue() = {}", cookie.getName(),
                            cookie.getValue()));
            log.error("세션에 Accessor 객체가 아닌 {} 값이 설정되어 있음", sessionAttribute);
            sendErrorResponse(response, 400, "세션에 " + sessionAttribute + " 값이 설정되어 있습니다.");
            return;
        }

        UserDetails principal = new User(accessor.getId(), "", accessor.getGrantedAuthorities());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                principal, "", accessor.getRole().getGrantedAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int sc, String msg) throws IOException {
        response.sendError(sc, msg);
    }

    private boolean isEmptySession(HttpSession session) {
        return session == null;
    }

    private Object getSessionAttribute(HttpSession session) {
        return session.getAttribute(SessionConst.LOGIN_USER);
    }
}
