package com.vn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vn.model.ChiTieu;
import com.vn.model.ThietBi;
import com.vn.repository.ChiTieuRepository;

@Controller
public class ChiTieuController {

    @Autowired
    ChiTieuRepository chiTieuRepository;

    @GetMapping("/admin/thuchi/chitieu-create")
    public String chitieuCreate(Model model) {
        model.addAttribute("chitieu", new ChiTieu());
        return "admin/chitieu/chitieu-create";
    }



}
