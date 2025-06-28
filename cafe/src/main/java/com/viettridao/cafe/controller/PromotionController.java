package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.mapper.PromotionMapper;
import com.viettridao.cafe.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {
    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;

    @GetMapping("")
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       Model model) {
        model.addAttribute("promotions", promotionService.getAllPromotions(page, size));
        return "/promotions/promotion";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("promotion", new CreatePromotionRequest());
        return "/promotions/create_promotion";
    }

    @PostMapping("/create")
    public String createPromotion(@Valid @ModelAttribute("promotion") CreatePromotionRequest promotion, BindingResult result, RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/promotions/create_promotion";
            }
            promotionService.createPromotion(promotion);
            redirectAttributes.addFlashAttribute("success", "Thêm khuyến mãi thành công");
            return "redirect:/promotion";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion/create";
        }
    }

    @PostMapping("/delete/{id}")
    public String deletePromotion(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            promotionService.deletePromotion(id);
            redirectAttributes.addFlashAttribute("success", "Xoá khuyến mãi thành công");
            return "redirect:/promotion";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }

    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try{
        	PromotionResponse response = promotionMapper.toDto(promotionService.getPromotionById(id));
            model.addAttribute("promotion", response);
            return "/promotions/update_promotion";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }

    @PostMapping("/update")
    public String updatePromotion(@Valid @ModelAttribute UpdatePromotionRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/promotions/update_promotion";
            }
            promotionService.updatePromotion(request);
            redirectAttributes.addFlashAttribute("success", "Chỉnh sửa khuyến mãi thành công");
            return "redirect:/promotion";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }

}
