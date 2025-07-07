package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller xử lý các request liên quan đến trang giới thiệu (About).
 * 
 * Thiết kế đơn giản:
 * - Chỉ xử lý hiển thị trang static về thông tin công ty/ứng dụng
 * - Không có business logic phức tạp, chỉ routing tới view
 * - Stateless controller, không cần dependency injection
 * 
 * Performance:
 * - Lightweight operation, chỉ return view name
 * - Có thể cache ở tầng view resolver nếu cần
 * 
 * @author Cafe Management Team
 * @since 1.0
 */
@Controller
@RequestMapping("/about")
public class AboutController {

    /**
     * Hiển thị trang giới thiệu của ứng dụng.
     * 
     * Endpoint đơn giản chỉ trả về static content về công ty,
     * thông tin ứng dụng, team phát triển, v.v.
     * 
     * Design decision: Không cần Model vì trang About thường là static content
     * được định nghĩa trực tiếp trong template.
     * 
     * @return đường dẫn tới template about.html trong thư mục abouts/
     */
    @GetMapping("")
    public String showAboutPage() {
        // Trả về view template - Spring sẽ resolve thành abouts/about.html
        return "abouts/about";
    }
}