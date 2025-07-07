package com.vn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vn.model.DonViTinh;
import com.vn.repository.HangHoaRepository;

@Controller
public class HangHoaController {

    @Autowired
    private HangHoaRepository hangHoaRepository;

    @Autowired
    private DonViTinhRepository donViTinhRepository;

        @GetMapping("/admin/hanghoa/hanghoa-create")
    public String showCreateHangHoaForm(Model model) {
        model.addAttribute("hangHoa", new com.vn.model.HangHoa());
        List<DonViTinh> donViTinhList = donViTinhRepository.findAll();
        model.addAttribute("donViTinhList", donViTinhList);
        return "admin/hanghoa/hanghoa-create";
    }


    
}
