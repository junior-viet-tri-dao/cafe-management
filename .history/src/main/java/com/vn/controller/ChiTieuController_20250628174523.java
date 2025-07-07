package com.vn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

        if (result.hasErrors()) {
            model.addAttribute("chiTieu", chiTieu);
            return "admin/thuchi/chitieu-create";
        }
        chiTieuRepository.save(chiTieu);
        return "redirect:/admin/thuchi/chitieu-list";
    }



}
