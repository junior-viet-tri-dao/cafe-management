package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PromotionController {

    @GetMapping("/marketing")
    public String showPromotion() {
        return "marketing/marketing";
    }

    @GetMapping("/marketing/create")
    public String showCreatePromotion() {
        return "marketing/marketing-create";
    }

    @GetMapping("/marketing/edit")
    public String showEditPromotion() {
        return "marketing/marketing-edit";
    }
}
