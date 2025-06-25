package com.viettridao.cafe.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.service.PromotionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("/marketing")
    public String showPromotion(Model model) {
        try {
            List<PromotionResponse> promotions = promotionService.getAllPromotions();
            model.addAttribute("listPromotion", promotions);

            // Debug log
            System.out.println("Số lượng promotions: " + (promotions != null ? promotions.size() : 0));
            if (promotions != null && !promotions.isEmpty()) {
                System.out.println("Promotion đầu tiên: " + promotions.get(0).getPromotionName());
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách promotion: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("listPromotion", null);
        }

        return "marketing/marketing";
    }

    @GetMapping("/marketing/edit/{id}")
    public String showEditPromotion(@PathVariable("id") Integer id, Model model,
            RedirectAttributes redirectAttributes) {
        // Validate ID
        if (id == null || id <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "ID khuyến mãi không hợp lệ");
            return "redirect:/marketing";
        }

        try {
            PromotionResponse promotion = promotionService.getPromotionById(id);
            if (promotion != null) {
                model.addAttribute("promotion", promotion);
                return "marketing/marketing-edit";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy khuyến mãi với ID: " + id);
                return "redirect:/marketing";
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy promotion với ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi tải thông tin khuyến mãi");
            return "redirect:/marketing";
        }
    }

    @PostMapping("/marketing/edit/{id}")
    public String editPromotion(@PathVariable("id") Integer id,
            @jakarta.validation.Valid @ModelAttribute UpdatePromotionRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        // Validate ID
        if (id == null || id <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "ID khuyến mãi không hợp lệ");
            return "redirect:/marketing";
        }

        // Set ID từ path variable vào request
        request.setId(id);

        // Kiểm tra validation errors
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());

            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ: " + String.join(", ", errors));

            // Lấy lại thông tin promotion để hiển thị form
            try {
                PromotionResponse promotion = promotionService.getPromotionById(id);
                model.addAttribute("promotion", promotion);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi tải thông tin khuyến mãi");
                return "redirect:/marketing";
            }

            return "marketing/marketing-edit";
        }

        try {
            // Gọi service để update
            promotionService.updatePromotion(request);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật khuyến mãi thành công!");
            return "redirect:/marketing";

        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());

            // Lấy lại thông tin promotion để hiển thị form
            try {
                PromotionResponse promotion = promotionService.getPromotionById(id);
                model.addAttribute("promotion", promotion);
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi tải thông tin khuyến mãi");
                return "redirect:/marketing";
            }

            return "marketing/marketing-edit";

        } catch (RuntimeException e) {
            System.err.println("Lỗi khi cập nhật promotion: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("errorMessage", e.getMessage());

            // Lấy lại thông tin promotion để hiển thị form
            try {
                PromotionResponse promotion = promotionService.getPromotionById(id);
                model.addAttribute("promotion", promotion);
            } catch (Exception ex) {
                redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi tải thông tin khuyến mãi");
                return "redirect:/marketing";
            }

            return "marketing/marketing-edit";

        } catch (Exception e) {
            System.err.println("Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();

            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật khuyến mãi");
            return "redirect:/marketing";
        }
    }

    @GetMapping("/marketing/create")
    public String showCreatePromotion() {
        return "marketing/marketing-create";
    }

    @PostMapping("/marketing/delete/{id}")
    public String deletePromotion(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.deletePromotion(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa khuyến mãi thành công!");
            return "redirect:/marketing";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/marketing";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/marketing";
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa promotion: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa khuyến mãi");
            return "redirect:/marketing";
        }
    }
}
