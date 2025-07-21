package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.account.LoginRequest;
import com.viettridao.cafe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("login", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("login") LoginRequest request, BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                return "login";
            }

            boolean result = authService.login(request.getUsername(), request.getPassword());
            if (result) {
                redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
                return "redirect:/home";
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("error", "Đăng nhập thất bại");
        return "redirect:/login";
    }
}
