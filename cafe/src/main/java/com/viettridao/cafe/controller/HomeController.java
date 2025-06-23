package com.viettridao.cafe.controller;

import com.viettridao.cafe.model.TaiKhoan;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {
    @GetMapping("")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan user = (TaiKhoan) auth.getPrincipal();
        model.addAttribute("user", user);
        return "layout";
    }
}
