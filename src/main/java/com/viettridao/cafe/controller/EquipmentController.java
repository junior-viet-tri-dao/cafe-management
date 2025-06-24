package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EquipmentController {

    @GetMapping("/device")
    public String showDevice() {
        return "devices/device";
    }

    @GetMapping("/device/create")
    public String showCreateDevice() {
        return "devices/device-create";
    }

}
