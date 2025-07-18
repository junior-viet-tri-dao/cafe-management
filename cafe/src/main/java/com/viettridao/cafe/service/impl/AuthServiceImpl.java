package com.viettridao.cafe.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.viettridao.cafe.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * AuthServiceImpl
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 */
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    // Quản lý xác thực
    private final AuthenticationManager authenticationManager;

    // Yêu cầu HTTP hiện tại
    private final HttpServletRequest request;

    /**
     * Xác thực thông tin đăng nhập của người dùng.
     *
     * @param username Tên đăng nhập của người dùng.
     * @param password Mật khẩu của người dùng.
     * @return true nếu thông tin đăng nhập hợp lệ, ngược lại false.
     * @throws RuntimeException Nếu thông tin đăng nhập không hợp lệ hoặc không đầy đủ
     */
    @Override
    public boolean login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ thông tin");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());
            return true;
        } catch (AuthenticationException e) {
            throw e;
        }
    }
}