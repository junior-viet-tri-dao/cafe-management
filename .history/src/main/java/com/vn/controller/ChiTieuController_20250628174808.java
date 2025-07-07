package com.vn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.vn.auth.CustomUserDetail;
import com.vn.model.ChiTieu;
import com.vn.model.ThietBi;
import com.vn.repository.ChiTieuRepository;

import jakarta.validation.Valid;

@Controller
public class ChiTieuController {

    @Autowired
    ChiTieuRepository chiTieuRepository;

    @GetMapping("/admin/thuchi/chitieu-create")
    public String chitieuCreate(Model model) {
        model.addAttribute("chiTieu", new ChiTieu());
        return "admin/thuchi/chitieu-create";
    }

    @PostMapping("/admin/thuchi/chitieu-create")
    public String createTable(@Valid @ModelAttribute("chiTieu") ChiTieu chiTieu, BindingResult result, Model model) {

        if(chiTieuRepository.existsByMaChiTieu(chiTieu.getMaChiTieu())) {
            result.rejectValue("maChiTieu", "error.chiTieu", "Mã chi tiêu đã tồn tại");
        }

        if(chiTieuRepository.existsByTenKhoanChi(chiTieu.getTenKhoanChi())) {
            result.rejectValue("tenKhoanChi", "error.chiTieu", "Tên khoản chi đã tồn tại");
        }

        if (chiTieu.getSoTien() == null || chiTieu.getSoTien() <= 0) {
            result.rejectValue("soTien", "error.chiTieu", "Số tiền phải lớn hơn 0");
        }
        if (result.hasErrors()) {
            model.addAttribute("chiTieu", chiTieu);
            return "admin/thuchi/chitieu-create";
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        chiTieu.setNhanVien(userDetail.getUsersDB());

        chiTieuRepository.save(chiTieu);
        return "redirect:/admin/thuchi/chitieu-list";
    }



}
