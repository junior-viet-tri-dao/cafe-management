package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.servlet.mvc.support.RedirectAttributes; 

import com.viettridao.cafe.service.AuthService; 

import lombok.RequiredArgsConstructor; 

/**
 * Controller xử lý các yêu cầu liên quan đến xác thực người dùng (đăng nhập).
 * Điều hướng người dùng đến form đăng nhập và xử lý dữ liệu đăng nhập.
 */
@Controller // Đánh dấu đây là một Spring MVC Controller
@RequiredArgsConstructor // Tự động tạo constructor để inject các dependency final (AuthService)
public class AuthController {

	// Inject AuthService để sử dụng các phương thức xử lý xác thực
	private final AuthService authService;

	/**
	 * Xử lý yêu cầu HTTP GET đến "/login". Phương thức này dùng để hiển thị form
	 * đăng nhập cho người dùng.
	 *
	 * @return Tên của view (trang HTML) là "login".
	 */
	@GetMapping("/login")
	public String showLoginForm() {
		return "login"; // Trả về tên view 'login.html' (hoặc tương tự)
	}

	/**
	 * Xử lý yêu cầu HTTP POST đến "/login". Phương thức này nhận tên đăng nhập và
	 * mật khẩu từ form và gọi dịch vụ xác thực.
	 *
	 * @param username           Tên đăng nhập được gửi từ form.
	 * @param password           Mật khẩu được gửi từ form.
	 * @param redirectAttributes Đối tượng dùng để thêm các thuộc tính "flash" (chỉ
	 *                           tồn tại một lần redirect) để hiển thị thông báo
	 *                           thành công hoặc lỗi sau khi chuyển hướng.
	 * @return Chuyển hướng đến trang chủ ("redirect:/home") nếu đăng nhập thành
	 *         công, hoặc chuyển hướng lại trang đăng nhập ("redirect:/login") kèm
	 *         thông báo lỗi nếu thất bại.
	 */
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password,
			RedirectAttributes redirectAttributes) {

		try {
			// Gọi AuthService để thực hiện logic đăng nhập
			boolean result = authService.login(username, password);
			if (result) {
				// Nếu đăng nhập thành công, thêm thông báo success và chuyển hướng đến trang
				// chủ
				redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
				return "redirect:/home";
			} else {
				// Nếu đăng nhập thất bại (do logic trong AuthService trả về false),
				// thêm thông báo lỗi và chuyển hướng lại trang đăng nhập
				redirectAttributes.addFlashAttribute("error", "Đăng nhập thất bại. Vui lòng kiểm tra lại.");
				return "redirect:/login";
			}
		} catch (Exception e) {
			// Bắt các ngoại lệ xảy ra trong quá trình đăng nhập (ví dụ: lỗi validation,
			// không tìm thấy người dùng)
			// Thêm thông báo lỗi và chuyển hướng lại trang đăng nhập
			redirectAttributes.addFlashAttribute("error", "Đăng nhập lỗi: " + e.getMessage());
			return "redirect:/login";
		}
	}
}