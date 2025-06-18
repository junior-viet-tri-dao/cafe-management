package com.viettridao.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.viettridao.cafe.model.TaiKhoan;
import com.viettridao.cafe.repository.TaiKhoanRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @GetMapping("/login")
    public String hienThiFormLogin(Model model) {
        model.addAttribute("taiKhoan", new TaiKhoan());
        return "login";
    }

    @PostMapping("/login")
    public String xuLyDangNhap(@ModelAttribute TaiKhoan taiKhoan, Model model, HttpSession session) {
        TaiKhoan tk = taiKhoanRepo.findByTenDangNhap(taiKhoan.getTenDangNhap());
        if (tk != null && tk.getMatKhau().equals(taiKhoan.getMatKhau())) {
            session.setAttribute("user", tk);
            return "redirect:/home";
        }
        model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
        return "login";
    }
}

