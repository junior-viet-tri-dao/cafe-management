package com.viettridao.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.service.EmployeeService;

import jakarta.validation.Valid;

/**
 * Controller xử lý các yêu cầu liên quan đến hồ sơ nhân viên.
 * Cung cấp các endpoint để xem và cập nhật thông tin hồ sơ.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    // Dịch vụ nhân viên để xử lý logic liên quan đến hồ sơ
    @Autowired
    private EmployeeService employeeService;

    /**
     * Hiển thị trang hồ sơ nhân viên.
     * @param model Đối tượng Model để thêm dữ liệu vào view
     * @param authentication Đối tượng Authentication chứa thông tin người dùng đã xác thực
     * @return Tên view "profile/view" để hiển thị trang hồ sơ
     */
    @GetMapping
    public String viewProfile(Model model, Authentication authentication) {
        // Lấy thông tin hồ sơ nhân viên dựa trên tên đăng nhập từ Authentication
        EmployeeProfileDTO dto = employeeService.getProfileDTO(authentication.getName());

        // Tạo đối tượng request để hiển thị thông tin trên form
        EmployeeProfileRequest request = new EmployeeProfileRequest();
        request.setId(dto.getId());
        request.setFullName(dto.getFullName());
        request.setPosition(dto.getPosition());
        request.setAddress(dto.getAddress());
        request.setPhoneNumber(dto.getPhoneNumber());
        request.setSalary(dto.getSalary());
        request.setImageUrl(dto.getImageUrl());

        // Thêm DTO và request vào model để sử dụng trong view
        model.addAttribute("employee", dto);
        model.addAttribute("request", request);
        return "profile/view";
    }

    /**
     * Xử lý yêu cầu cập nhật hồ sơ nhân viên.
     * @param request Đối tượng chứa dữ liệu cập nhật từ form
     * @param result Kết quả kiểm tra dữ liệu đầu vào
     * @param model Đối tượng Model để thêm dữ liệu vào view
     * @param authentication Đối tượng Authentication chứa thông tin người dùng đã xác thực
     * @return Tên view "profile/view" để hiển thị lại trang hồ sơ
     */
    @PostMapping
    public String updateProfile(@ModelAttribute("request") @Valid EmployeeProfileRequest request,
                                BindingResult result,
                                Model model,
                                Authentication authentication) {
        // Kiểm tra lỗi validate từ form
        if (result.hasErrors()) {
            // Lấy lại thông tin hồ sơ để hiển thị
            EmployeeProfileDTO dto = employeeService.getProfileDTO(authentication.getName());
            model.addAttribute("employee", dto);
            return "profile/view";
        }

        try {
            // Cập nhật hồ sơ nhân viên
            employeeService.updateProfile(request);
            // Thêm thông báo thành công vào model
            model.addAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            // Thêm thông báo lỗi vào model nếu có ngoại lệ
            model.addAttribute("error", "Lỗi: " + e.getMessage());
        }

        // Lấy thông tin hồ sơ đã cập nhật để hiển thị
        EmployeeProfileDTO updated = employeeService.getProfileDTO(authentication.getName());
        model.addAttribute("employee", updated);
        model.addAttribute("request", request);
        return "profile/view";
    }
}