package com.viettridao.cafe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

    @GetMapping("/restore")
    public String restore(Model model) {
        return "/datas/restore";
    }

    @GetMapping("/backup")
    public String backup(Model model) {
        return "/datas/backup";
    }
}
