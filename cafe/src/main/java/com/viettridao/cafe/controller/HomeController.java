package com.viettridao.cafe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller xử lý trang chủ của ứng dụng quản lý cafe.
 * 
 * Thiết kế đơn giản:
 * - Landing page sau khi user đăng nhập thành công
 * - Hiển thị dashboard hoặc layout chính của ứng dụng
 * - Không cần business logic phức tạp, chỉ routing
 * 
 * Navigation flow:
 * - User login → redirect đến /home
 * - Từ đây navigate đến các module khác (account, employee, etc.)
 * 
 * @author Cafe Management Team
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    /**
     * Hiển thị trang chủ/dashboard của ứng dụng.
     * 
     * Entry point chính sau khi authentication thành công.
     * Trả về layout template chứa navigation và structure cơ bản.
     * 
     * Design decision: Sử dụng layout template để consistent UI
     * across toàn bộ application.
     * 
     * @return template layout chính của ứng dụng
     */
    @GetMapping
    public String home() {
        // Trả về layout template - base structure cho toàn bộ app
        return "layout";
    }
}
