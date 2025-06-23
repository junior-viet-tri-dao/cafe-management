package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.model.NhanVien;
import com.viettridao.cafe.model.VaiTro;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/admindashboard")
    public String hienThiDashboard(HttpSession session, RedirectAttributes redirect) {
        NhanVien nv = (NhanVien) session.getAttribute("nv");
        if (nv == null || nv.getVaiTro() != VaiTro.ADMIN) {
            redirect.addFlashAttribute("error", "Bạn không có quyền truy cập.");
            return "redirect:/login";
        }
        return "admin/admindashboard";
    }
    
    @GetMapping("/nhanviendashboard")
    public String hienThiDashboardNhanVien(HttpSession session, Model model) {
        NhanVien nv = (NhanVien) session.getAttribute("nv");

        if (nv == null) {
            return "redirect:/login";
        }

        model.addAttribute("nv", nv);
        return "admin/nhanviendashboard";
    }
}

