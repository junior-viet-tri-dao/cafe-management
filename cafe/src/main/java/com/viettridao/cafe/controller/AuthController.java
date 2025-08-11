package com.viettridao.cafe.controller; // Khai báo gói cho lớp AuthController

import com.viettridao.cafe.service.AuthService; // Nhập giao diện AuthService từ gói service
import lombok.RequiredArgsConstructor; // Nhập chú thích RequiredArgsConstructor của Lombok để tạo constructor
import org.springframework.stereotype.Controller; // Nhập chú thích Controller của Spring để đánh dấu lớp là một controller
import org.springframework.web.bind.annotation.GetMapping; // Nhập chú thích GetMapping để xử lý yêu cầu GET
import org.springframework.web.bind.annotation.PostMapping; // Nhập chú thích PostMapping để xử lý yêu cầu POST
import org.springframework.web.bind.annotation.RequestParam; // Nhập chú thích RequestParam để liên kết tham số yêu cầu với tham số phương thức
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Nhập lớp RedirectAttributes để truyền thuộc tính flash sau khi chuyển hướng

/**
 * AuthController
 *
 * Lớp này xử lý các yêu cầu liên quan đến xác thực người dùng,
 * bao gồm hiển thị trang đăng nhập và xử lý logic đăng nhập.
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
public class AuthController {

    /**
     * Tiêm phụ thuộc AuthService để xử lý logic xác thực.
     */
    private final AuthService authService; // Khai báo một trường final cho AuthService, được tiêm qua constructor

    /**
     * Hiển thị trang đăng nhập.
     * Xử lý yêu cầu GET đến "/login".
     *
     * @return Tên của view (trang HTML) cho biểu mẫu đăng nhập.
     */
    @GetMapping("/login") // Ánh xạ yêu cầu GET đến đường dẫn "/login"
    public String showLoginForm() { // Định nghĩa phương thức để hiển thị trang đăng nhập
        return "login"; // Trả về tên của view "login.html"
    }

    /**
     * Xử lý yêu cầu đăng nhập từ biểu mẫu.
     * Xử lý yêu cầu POST đến "/login".
     *
     * @param username Tên người dùng được gửi từ biểu mẫu.
     * @param password Mật khẩu được gửi từ biểu mẫu.
     * @param redirectAttributes Đối tượng RedirectAttributes để thêm các thuộc tính flash
     * (ví dụ: thông báo thành công/lỗi) cho yêu cầu chuyển hướng.
     * @return Chuỗi chuyển hướng đến trang chủ nếu đăng nhập thành công,
     * hoặc chuyển hướng lại trang đăng nhập nếu đăng nhập thất bại.
     */
//    @PostMapping("/login") // Ánh xạ yêu cầu POST đến đường dẫn "/login"
//    public String login(@RequestParam String username, // Lấy giá trị của tham số "username" từ yêu cầu
//                        @RequestParam String password, // Lấy giá trị của tham số "password" từ yêu cầu
//                        RedirectAttributes redirectAttributes) { // Đối tượng để thêm thuộc tính flash cho chuyển hướng
//        try { // Bắt đầu khối try để xử lý ngoại lệ
//            boolean result = authService.login(username, password); // Gọi dịch vụ xác thực để kiểm tra tên người dùng và mật khẩu
//            if (result) { // Nếu kết quả đăng nhập là true (thành công)
//                redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!"); // Thêm thông báo thành công vào flash attributes
//                return "redirect:/home"; // Chuyển hướng người dùng đến trang "/home"
//            }
//        } catch (RuntimeException e) { // Bắt các ngoại lệ RuntimeException có thể xảy ra trong quá trình đăng nhập
//            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Thêm thông báo lỗi từ ngoại lệ vào flash attributes
//            return "redirect:/login"; // Chuyển hướng người dùng trở lại trang "/login"
//        }
//        redirectAttributes.addFlashAttribute("error", "Đăng nhập thất bại"); // Nếu không có ngoại lệ nhưng đăng nhập không thành công, thêm thông báo lỗi chung
//        return "redirect:/login"; // Chuyển hướng người dùng trở lại trang "/login"
//    }
}
