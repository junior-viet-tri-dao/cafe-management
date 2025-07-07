package com.vn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vn.model.ChucVu;
import com.vn.repository.HangHoaRepository;

@Controller
public class HangHoaController {

    @Autowired
    private HangHoaRepository hangHoaRepository;

        @GetMapping("/admin/hanghoa/hanghoa-create")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("user", new com.vn.model.Users());
        List<ChucVu> chucVuList = chucVuRepository.findAll();
        model.addAttribute("chucVuList", chucVuList);
        return "admin/employee/create";
    }


    
}
