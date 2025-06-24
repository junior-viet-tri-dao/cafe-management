package com.viettridao.cafe.controller;

import com.viettridao.cafe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller xử lý các yêu cầu liên quan đến xác thực người dùng. Cung cấp các
 * endpoint để hiển thị form đăng nhập và xử lý đăng nhập.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {
	// Dịch vụ xác thực để xử lý logic đăng nhập
	private final AuthService authService;

	/**
	 * Hiển thị form đăng nhập.
	 * 
	 * @return Tên view "login" để hiển thị trang đăng nhập
	 */
	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

	/**
	 * Xử lý yêu cầu đăng nhập từ người dùng.
	 * 
	 * @param username           Tên đăng nhập do người dùng cung cấp
	 * @param password           Mật khẩu do người dùng cung cấp
	 * @param redirectAttributes Đối tượng để thêm thông báo flash
	 * @return Chuyển hướng đến trang chủ nếu thành công, hoặc trang đăng nhập nếu
	 *         thất bại
	 */
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password,
			RedirectAttributes redirectAttributes) {
		try {
			// Gọi dịch vụ xác thực để kiểm tra thông tin đăng nhập
			boolean result = authService.login(username, password);
			if (result) {
				// Thêm thông báo thành công vào flash attribute
				redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
				// Chuyển hướng đến trang chủ
				return "redirect:/home";
			}
		} catch (RuntimeException e) {
			// Thêm thông báo lỗi từ ngoại lệ vào flash attribute
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			// Chuyển hướng về trang đăng nhập
			return "redirect:/login";
		}
		// Thêm thông báo lỗi mặc định nếu đăng nhập thất bại
		redirectAttributes.addFlashAttribute("error", "Đăng nhập thất bại");
		// Chuyển hướng về trang đăng nhập
		return "redirect:/login";
	}
}