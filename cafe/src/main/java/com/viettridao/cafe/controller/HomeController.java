package com.viettridao.cafe.controller; // Khai báo gói cho lớp HomeController

import lombok.RequiredArgsConstructor; // Nhập chú thích RequiredArgsConstructor của Lombok để tạo constructor
import org.springframework.stereotype.Controller; // Nhập chú thích Controller của Spring để đánh dấu lớp là một controller
import org.springframework.web.bind.annotation.GetMapping; // Nhập chú thích GetMapping để xử lý yêu cầu GET

/**
 * HomeController
 *
 * Lớp này xử lý các yêu cầu liên quan đến trang chủ của ứng dụng.
 *
 * Phiên bản 1.0
 *
 * Ngày: 2025-07-23
 *
 * Bản quyền (c) 2025 VietTriDao. Đã đăng ký bản quyền.
 *
 * Nhật ký sửa đổi:
 * NGÀY                 TÁC GIẢ          MÔ TẢ
 * -----------------------------------------------------------------------
 * 2025-07-23           Hoa Mãn Lâu     Tạo và định dạng ban đầu.
 */
@Controller // Đánh dấu lớp này là một Spring MVC Controller
@RequiredArgsConstructor // Tự động tạo constructor với các trường final
public class HomeController {

    /**
     * Hiển thị trang chủ của ứng dụng.
     * Xử lý yêu cầu GET đến "/home".
     *
     * @return Tên của view (trang HTML) cho trang chủ.
     */
    @GetMapping("/home") // Ánh xạ yêu cầu GET đến đường dẫn "/home"
    public String home() { // Định nghĩa phương thức để hiển thị trang chủ
        return "layout"; // Trả về tên của view "layout.html" (thường là trang chính hoặc bố cục chung của ứng dụng)
    }
}
