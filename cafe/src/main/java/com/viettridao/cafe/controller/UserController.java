package com.viettridao.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.Service.NhanVienService;
import com.viettridao.cafe.model.NhanVien;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/home")
public class UserController {

    @Autowired
    private NhanVienService service;

    @GetMapping("")
    public String home(HttpSession session, Model model) {
        NhanVien nv = (NhanVien) session.getAttribute("nv");
        if (nv == null) return "redirect:/login";
        model.addAttribute("nv", nv);
        return "TrangChu/home";
    }

    @GetMapping("/canhan")
    public String profile(HttpSession session, Model model) {
        NhanVien nv = (NhanVien) session.getAttribute("nv");
        if (nv == null) return "redirect:/login";
        model.addAttribute("nv", nv);
        return "TrangCaNhan/canhan";
    }

    @PostMapping("/canhan")
    public String updateProfile(@ModelAttribute("nv") NhanVien formNv,
                                HttpSession session) {
        NhanVien nv = (NhanVien) session.getAttribute("nv");
        if (nv == null) return "redirect:/login";

        nv.setHoVaTen(formNv.getHoVaTen());
        nv.setChucVu(formNv.getChucVu());
        nv.setDiaChi(formNv.getDiaChi());
        nv.setSdt(formNv.getSdt());
        nv.setLuong(formNv.getLuong());

        service.save(nv);
        return "redirect:/home/canhan";
    }
}
