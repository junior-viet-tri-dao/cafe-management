package com.viettridao.cafe.controller;

import com.viettridao.cafe.Service.NhanVienService;
import com.viettridao.cafe.model.NhanVien;
import com.viettridao.cafe.model.VaiTro;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private NhanVienService service;

    @GetMapping("/login")
    public String loginPage() {
        return "TrangChu/login"; // View: templates/TrangChu/login.html
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String user,
                          @RequestParam String pass,
                          HttpSession session,
                          Model model) {

        // ✅ Kiểm tra rỗng
        if (user.isBlank() || pass.isBlank()) {
            model.addAttribute("error", "Không được để trống");
            return "TrangChu/login";
        }

        // ✅ Xác thực tài khoản
        NhanVien nv = service.login(user, pass);
        if (nv == null) {
            model.addAttribute("error", "Sai tên đăng nhập hoặc mật khẩu");
            return "TrangChu/login";
        }
        if (nv.getVaiTro() == null) {
            model.addAttribute("error", "Tài khoản chưa được gán vai trò. Vui lòng liên hệ quản trị viên.");
            return "TrangChu/login";
        }
        // ✅ Lưu vào session
        session.setAttribute("nv", nv);
        session.setAttribute("vaiTro", nv.getVaiTro() != null ? nv.getVaiTro().name() : "");

        // ✅ Phân quyền chuyển hướng
        if (nv.getVaiTro() == VaiTro.ADMIN) {
            return "redirect:/home"; // hoặc redirect:/admin/dashboard nếu có
        } else if (nv.getVaiTro() == VaiTro.NHANVIEN) {
            return "redirect:/admin/nhanviendashboard";
        } else {
            model.addAttribute("error", "Tài khoản chưa được gán vai trò!");
            return "TrangChu/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
