package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionPageResponse;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.mapper.PromotionMapper;
import com.viettridao.cafe.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý chương trình khuyến mãi và ưu đãi.
 * 
 * Business context:
 * - Quản lý lifecycle của các promotion campaigns
 * - Validation thời gian hiệu lực (start date, end date)
 * - Tích hợp với hệ thống POS để apply discounts
 * 
 * Thiết kế patterns:
 * - CRUD operations với validation
 * - Pagination cho performance với large datasets
 * - Filtering active/valid promotions by date range
 * - POST-redirect-GET để tránh duplicate submissions
 * 
 * Performance considerations:
 * - Pagination để handle large promotion history
 * - Indexing theo date range cho query optimization
 * - Cache valid promotions nếu cần real-time performance
 * 
 * @author Cafe Management Team
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {

    // Core dependencies
    private final PromotionService promotionService;
    private final PromotionMapper promotionMapper;

    /**
     * Hiển thị danh sách promotion đang có hiệu lực.
     * 
     * Business logic: Chỉ hiển thị promotions trong thời gian hiệu lực
     * (current date between start_date và end_date).
     * 
     * Pagination strategy:
     * - Default page size 10 để balance performance vs UX
     * - Service layer handle filtering active promotions
     * - PageResponse pattern để consistent API
     * 
     * @param page  số trang hiện tại (0-indexed)
     * @param size  số records mỗi trang
     * @param model Spring MVC model
     * @return view template danh sách promotions
     */
    @GetMapping("")
    public String getActivePromotions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        // Tạo Pageable object cho Spring Data pagination
        Pageable pageable = PageRequest.of(page, size);

        // Service layer filter valid promotions by business rules
        PromotionPageResponse promotions = promotionService.getValidPromotions(pageable);

        // Populate model cho view rendering
        model.addAttribute("promotions", promotions.getPromotions());
        model.addAttribute("currentPage", promotions.getPageNumber());
        model.addAttribute("totalPages", promotions.getTotalPages());

        return "promotions/promotion";
    }

    /**
     * Hiển thị form tạo promotion mới.
     * 
     * Form setup: Empty CreatePromotionRequest object cho Spring form binding.
     * 
     * @param model Spring MVC model
     * @return view template form create promotion
     */
    @GetMapping("/create")
    public String showFormCreate(Model model) {
        // Empty DTO cho form binding
        model.addAttribute("promotion", new CreatePromotionRequest());
        return "/promotions/create_promotion";
    }

    /**
     * Xử lý tạo promotion mới.
     * 
     * Validation layers:
     * 1. Bean validation với @Valid annotation
     * 2. Business rules validation trong service layer
     * 3. Date range validation (start < end, future dates)
     * 
     * @param promotion          request DTO với promotion data
     * @param result             validation binding result
     * @param redirectAttributes flash attributes cho messages
     * @return redirect URL hoặc view name với errors
     */
    @PostMapping("/create")
    public String createPromotion(@Valid @ModelAttribute("promotion") CreatePromotionRequest promotion,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation check - reload form nếu có errors
            if (result.hasErrors()) {
                return "/promotions/create_promotion";
            }

            // Delegate business logic cho service layer
            promotionService.createPromotion(promotion);

            // Success feedback
            redirectAttributes.addFlashAttribute("success", "Thêm khuyến mãi thành công");
            return "redirect:/promotion";

        } catch (Exception e) {
            // Error handling với user-friendly messages
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion/create";
        }
    }

    /**
     * Xóa promotion theo ID.
     * 
     * Business considerations:
     * - Soft delete để preserve audit trail
     * - Validation: không được xóa promotion đang active
     * - Rollback transactions nếu có orders đã sử dụng promotion
     * 
     * @param id                 ID của promotion cần xóa
     * @param redirectAttributes flash attributes cho messages
     * @return redirect về danh sách promotions
     */
    @PostMapping("/delete/{id}")
    public String deletePromotion(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            // Service layer handle business validation
            promotionService.deletePromotion(id);

            // Success feedback
            redirectAttributes.addFlashAttribute("success", "Xóa khuyến mãi thành công");
            return "redirect:/promotion";

        } catch (Exception e) {
            // Error feedback - có thể do promotion đang được sử dụng
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }

    /**
     * Hiển thị form update promotion.
     * 
     * Data loading:
     * - Fetch promotion by ID
     * - Convert entity sang response DTO cho view
     * - Pre-populate form với existing data
     * 
     * @param id                 ID của promotion cần update
     * @param model              Spring MVC model
     * @param redirectAttributes flash attributes cho error handling
     * @return view template form update
     */
    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Fetch và convert sang DTO
            PromotionResponse response = promotionMapper.toPromotionResponse(promotionService.getPromotionById(id));

            // Pre-populate form
            model.addAttribute("promotion", response);
            return "/promotions/update_promotion";

        } catch (Exception e) {
            // Handle case: promotion không tồn tại hoặc access denied
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }

    /**
     * Xử lý update promotion.
     * 
     * Validation considerations:
     * - Date range validation (start < end)
     * - Business rules: không được edit promotion đã hết hạn
     * - Consistency: discount amount/percentage validation
     * 
     * @param request            update request DTO
     * @param result             validation binding result
     * @param redirectAttributes flash attributes
     * @return redirect URL hoặc view với errors
     */
    @PostMapping("/update")
    public String updatePromotion(@Valid @ModelAttribute UpdatePromotionRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation check
            if (result.hasErrors()) {
                return "/promotions/update_promotion";
            }

            // Delegate update logic cho service layer
            promotionService.updatePromotion(request);

            // Success feedback
            redirectAttributes.addFlashAttribute("success", "Cập nhật khuyến mãi thành công");
            return "redirect:/promotion";

        } catch (Exception e) {
            // Error feedback
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/promotion";
        }
    }
}
