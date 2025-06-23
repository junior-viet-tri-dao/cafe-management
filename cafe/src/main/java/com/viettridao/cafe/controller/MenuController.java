package com.viettridao.cafe.controller;

import com.viettridao.cafe.model.DonViTinh;
import com.viettridao.cafe.repository.DonViRepository;
import com.viettridao.cafe.service.ThucDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final ThucDonService thucDonService;
    private final DonViRepository donViRepository;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("menuItems", thucDonService.getAllMenus());
        return "/menu/menu";
    }

    @GetMapping("/create")
    public String showFormCreateMenu(Model model) {
        model.addAttribute("units", donViRepository.getAllUnits());
        return "/menu/create_menu";
    }
}
