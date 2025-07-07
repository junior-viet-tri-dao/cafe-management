package com.vn.controller;

import com.vn.model.Ban;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BanController {

    @GetMapping("/admin/sales/table-create")
    public String showCreateTableForm(Model model) {
        model.addAttribute("table", new Ban());
        return "admin/sales/table-create";
    }
}