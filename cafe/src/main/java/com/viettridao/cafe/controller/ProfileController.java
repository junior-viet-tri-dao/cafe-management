package com.viettridao.cafe.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các yêu cầu liên quan đến quản lý hồ sơ cá nhân của nhân
 * viên. Cho phép người dùng xem và cập nhật thông tin của chính mình.
 */
@Controller // Đánh dấu đây là một Spring MVC Controller
@RequestMapping("/profile") // Đặt base path cho tất cả các endpoint trong controller này là "/profile"
@RequiredArgsConstructor // Tự động tạo constructor để inject các dependency final (EmployeeService)
public class ProfileController {

	// Inject EmployeeService để sử dụng các phương thức xử lý nghiệp vụ liên quan
	// đến nhân viên
	private final EmployeeService employeeService;

	/**
	 * Xử lý yêu cầu HTTP GET đến "/profile". Phương thức này dùng để hiển thị thông
	 * tin hồ sơ của nhân viên hiện tại.
	 *
	 * @param model          Đối tượng Model để truyền dữ liệu hồ sơ đến view.
	 * @param authentication Đối tượng Authentication của Spring Security, cung cấp
	 *                       thông tin về người dùng đang đăng nhập.
	 * @param request        Đối tượng EmployeeProfileRequest, có thể chứa dữ liệu
	 *                       được truyền từ một POST redirect (ví dụ: lỗi
	 *                       validation).
	 * @return Tên view của trang hồ sơ cá nhân ("profile/view").
	 */
	@GetMapping // Xử lý yêu cầu GET cho base path "/profile"
	public String viewProfile(Model model, Authentication authentication,
			@ModelAttribute("request") EmployeeProfileRequest request) {

		// Lấy thông tin hồ sơ của nhân viên dưới dạng DTO dựa trên tên đăng nhập của
		// người dùng hiện tại
		EmployeeProfileDTO dto = employeeService.getProfileDTO(authentication.getName());

		// Nếu đối tượng request không có ID (tức là lần đầu truy cập GET hoặc không có
		// lỗi từ POST trước đó),
		// gán dữ liệu từ DTO vào request để hiển thị trên form.
		if (request.getId() == null) {
			request.setId(dto.getId());
			request.setFullName(dto.getFullName());
			request.setPosition(dto.getPosition());
			request.setAddress(dto.getAddress());
			request.setPhoneNumber(dto.getPhoneNumber());
			request.setSalary(dto.getSalary());
			request.setImageUrl(dto.getImageUrl());
			// Mật khẩu không được gán trực tiếp vào request để tránh hiển thị trong form
			// hoặc log
		}

		// Thêm đối tượng DTO chứa thông tin hồ sơ đầy đủ vào model
		model.addAttribute("employee", dto);
		// Thêm đối tượng request (có thể đã được điền hoặc rỗng) vào model để binding
		// với form
		model.addAttribute("request", request);
		return "profile/view"; // Trả về view để hiển thị trang hồ sơ
	}

	/**
	 * Xử lý yêu cầu HTTP POST đến "/profile". Phương thức này nhận dữ liệu từ form
	 * cập nhật hồ sơ và thực hiện cập nhật thông tin nhân viên.
	 *
	 * @param request            Đối tượng EmployeeProfileRequest chứa thông tin hồ
	 *                           sơ đã chỉnh sửa, được @Valid kiểm tra ràng buộc.
	 * @param result             Đối tượng BindingResult chứa kết quả của quá trình
	 *                           validation.
	 * @param redirectAttributes Đối tượng dùng để thêm thuộc tính flash cho
	 *                           redirect (thông báo thành công/thất bại).
	 * @param authentication     Đối tượng Authentication để lấy tên người dùng hiện
	 *                           tại (nếu cần).
	 * @return Chuyển hướng đến trang hồ sơ cá nhân sau khi xử lý cập nhật.
	 */
	@PostMapping // Xử lý yêu cầu POST cho base path "/profile"
	public String updateProfile(@Valid @ModelAttribute("request") EmployeeProfileRequest request, BindingResult result,
			RedirectAttributes redirectAttributes, Authentication authentication) {
		// Kiểm tra nếu có lỗi validation từ @Valid
		if (result.hasErrors()) {
			// Nếu có lỗi, thêm BindingResult và request vào flash attributes để chúng có
			// thể được truy cập
			// sau khi redirect, giúp hiển thị lại dữ liệu đã nhập và các thông báo lỗi trên
			// form.
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.request", result);
			redirectAttributes.addFlashAttribute("request", request);
			redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại thông tin.");
			return "redirect:/profile"; // Chuyển hướng về trang hồ sơ để hiển thị lỗi
		}

		try {
			// Gọi dịch vụ để cập nhật hồ sơ nhân viên
			employeeService.updateProfile(request);
			// Thêm thông báo thành công vào flash attribute
			redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
		} catch (Exception e) {
			// Nếu có lỗi nghiệp vụ (ví dụ: lỗi từ service), thêm thông báo lỗi vào flash
			// attribute
			redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
		}

		return "redirect:/profile"; // Luôn chuyển hướng về trang hồ sơ sau khi xử lý POST
	}
}