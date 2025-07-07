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
    public String createTable(@Valid @ModelAttribute("thietBi") ThietBi thietBi, BindingResult result, Model model) {
        
        if(thietBiRepository.existsByMaThietBi(thietBi.getMaThietBi())) {
            result.rejectValue("maThietBi", "error.thietBi", "Mã thiết bị đã tồn tại");
        }

        if(thietBiRepository.existsByTenThietBi(thietBi.getTenThietBi())) {
            result.rejectValue("tenThietBi", "error.thietBi", "Tên thiết bị đã tồn tại");
        }

        if (result.hasErrors()) {
            model.addAttribute("thietBi", thietBi);
            return "admin/thietbi/thietbi-create";
        }
        thietBiRepository.save(thietBi);  
        return "redirect:/admin/thietbi/thietbi-list";
    }



}
