package org.example.expert.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.auth.exception.AuthException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Slf4j
public class AdminAccessInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userRole = (String) request.getAttribute("userRole");

        if (!"ADMIN".equals(userRole)) {
            throw new AuthException("관리자 권한이 없습니다.");
        }

        log.info("[ADMIN 접근] 요청 시각={}, URL={}", LocalDateTime.now(), request.getRequestURI());
        return true;
    }
}