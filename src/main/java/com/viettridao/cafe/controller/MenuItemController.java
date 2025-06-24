package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MenuItemController {

    @GetMapping("/menu")
    public String showMenu() {
        return "menus/menu";
    }

    @GetMapping("/menu/create")
    public String showCreateMenu() {
        return "menus/menu-create";
    }

    @GetMapping("/menu/edit")
    public String showEditMenu() {
        return "menus/menu-edit";
    }
}
