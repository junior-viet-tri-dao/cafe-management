package com.viettridao.cafe.controller;

import com.viettridao.cafe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý authentication và authorization cho ứng dụng.
 * 
 * Quyết định thiết kế:
 * - Sử dụng Spring Security cho core authentication
 * - Controller chỉ handle UI presentation layer
 * - AuthService xử lý business logic liên quan đến auth
 * 
 * Security considerations:
 * - Login form được bảo vệ CSRF token
 * - Redirect safety để tránh open redirect attacks
 * - Session management được Spring Security handle
 * 
 * Flow: Login form → Spring Security filter → Success redirect → /home
 * 
 * @author Cafe Management Team
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

    // AuthService để xử lý business logic authentication nếu cần
    private final AuthService authService;

    /**
     * Hiển thị form đăng nhập cho user.
     * 
     * Endpoint này chỉ render login form, việc xử lý authentication
     * thực tế được Spring Security handle thông qua security config.
     * 
     * Design pattern: Separation of concerns
     * - Controller: UI presentation
     * - Spring Security: Authentication logic
     * - AuthService: Custom business rules nếu cần
     * 
     * @return đường dẫn tới login template
     */
    @GetMapping("")
    public String showLoginForm() {
        // Trả về login form template
        // Spring Security sẽ handle POST /login automatically
        return "auth/login";
    }
}
