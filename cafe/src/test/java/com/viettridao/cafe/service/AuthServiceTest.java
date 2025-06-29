package com.viettridao.cafe.service;

import com.viettridao.cafe.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Đăng nhập thành công")
    void testLogin_Success() {
        when(request.getSession(true)).thenReturn(session);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        boolean result = authService.login("user", "pass");
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Đăng nhập thất bại do thiếu thông tin")
    void testLogin_MissingInfo() {
        assertThatThrownBy(() -> authService.login("", "pass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Vui lòng nhập đầy đủ thông tin");
        assertThatThrownBy(() -> authService.login("user", null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Vui lòng nhập đầy đủ thông tin");
    }

    @Test
    @DisplayName("Đăng nhập thất bại do sai tài khoản hoặc mật khẩu")
    void testLogin_InvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Sai") {
                });
        assertThatThrownBy(() -> authService.login("user", "wrongpass"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Tên đăng nhập hoặc mật khẩu không hợp lệ");
    }
}
