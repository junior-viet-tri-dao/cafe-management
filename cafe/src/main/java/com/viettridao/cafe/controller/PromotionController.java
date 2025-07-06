package com.viettridao.cafe.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.viettridao.cafe.dto.request.promotion.PromotionCreateRequest;
import com.viettridao.cafe.dto.request.promotion.PromotionUpdateRequest;
import com.viettridao.cafe.service.promotion.IPromotionService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {

    private final IPromotionService promotionService;

    @GetMapping
    public String listPromotion(Model model) {
        model.addAttribute("promotions", promotionService.getPromotionAll());
        return "promotion/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("promotion", new PromotionCreateRequest());
        return "promotion/form-create";
    }

    @PostMapping
    public String createPromotion(@Valid @ModelAttribute("promotion") PromotionCreateRequest request,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            return "promotion/form-create";
        }
        promotionService.createPromotion(request);
        return "redirect:/promotion";
    }


    @GetMapping("/{id}/edit")
    public String showEditForm (@PathVariable Integer id, Model model) {
        PromotionUpdateRequest updateRequest = promotionService.getUpdateForm(id);
        model.addAttribute("promotion", updateRequest);
        model.addAttribute("id", id);
        return "promotion/form-edit";
    }

    @PostMapping("/{id}")
    public String updatePromotion(@PathVariable Integer id,
                                  @Valid @ModelAttribute("promotion") PromotionUpdateRequest request,
                                  BindingResult bindingResult,
                                  Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("id", id);
            return "promotion/form-edit";
        }

        promotionService.updatePromotion(id, request);
        return "redirect:/promotion";
    }


    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Integer id) {
        promotionService.deletePromotion(id);
        return "redirect:/promotion";
    }
}
