package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.viettridao.cafe.model.TaiKhoan;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String trangChu(HttpSession session, Model model) {
        TaiKhoan tk = (TaiKhoan) session.getAttribute("user");
        if (tk == null) return "redirect:/login";
        model.addAttribute("ten", tk.getTenDangNhap());
        return "home";
    }
}

