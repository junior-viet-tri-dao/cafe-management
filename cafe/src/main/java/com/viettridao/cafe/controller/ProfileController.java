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

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public String viewProfile(Model model, Authentication authentication) {
        EmployeeProfileDTO dto = employeeService.getProfileDTO(authentication.getName());

        EmployeeProfileRequest request = new EmployeeProfileRequest();
        request.setId(dto.getId());
        request.setFullName(dto.getFullName());
        request.setPosition(dto.getPosition());
        request.setAddress(dto.getAddress());
        request.setPhoneNumber(dto.getPhoneNumber());
        request.setSalary(dto.getSalary());
        request.setImageUrl(dto.getImageUrl());

        model.addAttribute("employee", dto);
        model.addAttribute("request", request);
        return "profile/view";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("request") @Valid EmployeeProfileRequest request,
                                BindingResult result,
                                Model model,
                                Authentication authentication) {
        if (result.hasErrors()) {
            EmployeeProfileDTO dto = employeeService.getProfileDTO(authentication.getName());
            model.addAttribute("employee", dto);
            return "profile/view";
        }

        try {
            employeeService.updateProfile(request);
            model.addAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi: " + e.getMessage());
        }

        EmployeeProfileDTO updated = employeeService.getProfileDTO(authentication.getName());
        model.addAttribute("employee", updated);
        model.addAttribute("request", request);
        return "profile/view";
    }
}
