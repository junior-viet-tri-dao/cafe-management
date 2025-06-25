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

import com.viettridao.cafe.controller.Request.ProfileUpdateRequest;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các yêu cầu liên quan đến hồ sơ cá nhân của nhân viên. Cung
 * cấp chức năng xem và cập nhật thông tin hồ sơ của người dùng đang đăng nhập.
 */
@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

	// Dịch vụ nhân viên để xử lý logic nghiệp vụ liên quan đến hồ sơ
	private final EmployeeService employeeService;

	/**
	 * Hiển trị trang thông tin hồ sơ cá nhân của nhân viên đang đăng nhập. * @param
	 * model Model để truyền dữ liệu tới view.
	 * 
	 * @param authentication Đối tượng chứa thông tin xác thực của người dùng hiện
	 *                       tại.
	 * @param request        Đối tượng ProfileUpdateRequest để giữ dữ liệu biểu mẫu.
	 * @return Tên view để hiển thị trang hồ sơ.
	 */
	@GetMapping
	public String viewProfile(Model model, Authentication authentication,
			@ModelAttribute("request") ProfileUpdateRequest request) {

		// Lấy thông tin hồ sơ nhân viên dựa trên tên đăng nhập
		EmployeeProfileDTO dto = employeeService.getProfileDTO(authentication.getName());

		// Nếu request chưa được điền, gán dữ liệu từ DTO
		if (request.getId() == null) {
			request.setId(dto.getId());
			request.setFullName(dto.getFullName());
			request.setPosition(dto.getPosition());
			request.setAddress(dto.getAddress());
			request.setPhoneNumber(dto.getPhoneNumber());
			request.setSalary(dto.getSalary());
			// Không có thuộc tính ngày tháng nào cần xử lý ở đây.
		}

		// Thêm dữ liệu vào model để hiển thị trên view
		model.addAttribute("employee", dto);
		model.addAttribute("request", request);
		return "profile/view";
	}

	/**
	 * Xử lý yêu cầu cập nhật hồ sơ cá nhân từ form. * @param request Đối tượng
	 * ProfileUpdateRequest chứa dữ liệu từ form cập nhật.
	 * 
	 * @param result             Đối tượng BindingResult để kiểm tra lỗi validation.
	 * @param redirectAttributes RedirectAttributes để thêm flash attributes cho các
	 *                           redirect.
	 * @param authentication     Đối tượng chứa thông tin xác thực của người dùng
	 *                           hiện tại.
	 * @return Chuỗi redirect đến trang hồ sơ sau khi xử lý.
	 */
	@PostMapping
	public String updateProfile(@Valid @ModelAttribute("request") ProfileUpdateRequest request, BindingResult result,
			RedirectAttributes redirectAttributes, Authentication authentication) {

		// Kiểm tra lỗi validation
		if (result.hasErrors()) {
			/*
			 * Lưu thông tin lỗi và request vào flash attributes để hiển thị lại trên trang
			 * khi redirect.
			 */
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.request", result);
			redirectAttributes.addFlashAttribute("request", request);
			redirectAttributes.addFlashAttribute("error", "Vui lòng kiểm tra lại thông tin.");
			return "redirect:/profile";
		}

		try {
			// Cập nhật thông tin hồ sơ
			employeeService.updateProfile(request);
			// Thêm thông báo thành công
			redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
		} catch (Exception e) {
			// Thêm thông báo lỗi nếu cập nhật thất bại
			redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
		}

		return "redirect:/profile";
	}
}