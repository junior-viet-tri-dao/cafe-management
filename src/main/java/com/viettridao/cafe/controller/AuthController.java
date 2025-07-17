package com.viettridao.cafe.controller;

import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController extends BaseController {
    private final AuthService authService;
    private final AccountService accountService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            boolean result = authService.login(username, password);

            if (result) {
                AccountEntity userInfo = accountService.findByUsername(username);
                session.setAttribute("user", userInfo);
                session.setAttribute("isAuthenticated", true);

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

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate(); // Xóa toàn bộ session
        redirectAttributes.addFlashAttribute("success", "Đăng xuất thành công!");
        return "redirect:/login";
    }
}
