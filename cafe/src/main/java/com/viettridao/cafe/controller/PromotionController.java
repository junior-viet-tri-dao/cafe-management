package com.viettridao.cafe.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionPageResponse;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.mapper.PromotionMapper;
import com.viettridao.cafe.service.PromotionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * PromotionController
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {

    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;

    /**
     * Hiển thị danh sách promotion đang có hiệu lực (phân trang).
     */
    @GetMapping("")
    public String getActivePromotions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size); // Tạo Pageable cho phân trang
        PromotionPageResponse promotions = promotionService.getValidPromotions(pageable); // Lấy promotions hợp lệ
        model.addAttribute("promotions", promotions.getPromotions());
        model.addAttribute("currentPage", promotions.getPageNumber());
        model.addAttribute("totalPages", promotions.getTotalPages());
        return "promotions/promotion";
    }

    /**
     * Hiển thị form tạo promotion mới.
     */
    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("promotion", new CreatePromotionRequest());
        return "/promotions/create_promotion";
    }

    /**
     * Xử lý tạo promotion mới.
     */
    @PostMapping("/create")
    public String createPromotion(@Valid @ModelAttribute("promotion") CreatePromotionRequest promotion,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Nếu có lỗi toàn cục, gán vào endDate để hiển thị dưới input
            if (result.hasGlobalErrors()) {
                for (var error : result.getGlobalErrors()) {
                    result.rejectValue("endDate", error.getCode(), error.getDefaultMessage());
                }
            }
            if (result.hasErrors()) {
                return "/promotions/create_promotion";
            }
            promotionService.createPromotion(promotion);
            redirectAttributes.addFlashAttribute("success", "Thêm khuyến mãi thành công");
            return "redirect:/promotion";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion/create";
        }
    }

    /**
     * Xóa promotion theo ID.
     */
    @PostMapping("/delete/{id}")
    public String deletePromotion(@PathVariable("id") Integer id,
                                  RedirectAttributes redirectAttributes) {
        try {
            promotionService.deletePromotion(id);
            redirectAttributes.addFlashAttribute("success", "Xóa khuyến mãi thành công");
            return "redirect:/promotion";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }

    /**
     * Hiển thị form update promotion.
     */
    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        try {
            PromotionResponse response = promotionMapper.toPromotionResponse(promotionService.getPromotionById(id));
            model.addAttribute("promotion", response);
            return "/promotions/update_promotion";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }

    /**
     * Xử lý update promotion.
     */
    @PostMapping("/update")
    public String updatePromotion(@Valid @ModelAttribute("promotion") UpdatePromotionRequest request,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        try {
            // Nếu có lỗi toàn cục, gán vào endDate để hiển thị dưới input
            if (result.hasGlobalErrors()) {
                for (var error : result.getGlobalErrors()) {
                    result.rejectValue("endDate", error.getCode(), error.getDefaultMessage());
                }
            }
            if (result.hasErrors()) {
                model.addAttribute("promotion", request);
                return "/promotions/update_promotion";
            }
            promotionService.updatePromotion(request);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khuyến mãi thành công");
            return "redirect:/promotion";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }
}