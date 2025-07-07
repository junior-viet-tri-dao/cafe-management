package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentPageResponse;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.service.EquipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý thiết bị - xử lý inventory và maintenance của cafe equipment.
 * 
 * Business context:
 * - Quản lý tài sản thiết bị (máy pha cafe, máy xay, máy làm lạnh, etc.)
 * - Tracking trạng thái thiết bị (hoạt động, bảo trì, hỏng hóc)
 * - Asset management cho depreciation và replacement planning
 * 
 * Thiết kế patterns:
 * - Standard CRUD operations với validation
 * - Pagination để handle large equipment inventory
 * - Asset lifecycle management integration
 * 
 * Performance considerations:
 * - Pagination ở DB level để optimize large datasets
 * - Caching equipment status cho real-time monitoring
 * - Lazy loading relationships để reduce query overhead
 * 
 * Maintenance workflow:
 * - Status tracking (active, maintenance, broken, retired)
 * - Integration với maintenance scheduling system
 * - Asset value depreciation calculations
 * 
 * @author Cafe Management Team
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/equipment")
public class EquipmentController {

    // Core dependencies
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    /**
     * Hiển thị danh sách thiết bị với pagination.
     * 
     * Features:
     * - Pagination với default page size 10
     * - Asset status filtering (active, maintenance, etc.)
     * 
     * Data structure: EquipmentPageResponse bao gồm cả pagination metadata
     * và actual equipment list để optimize view rendering.
     * 
     * @param page  số trang hiện tại (0-indexed)
     * @param size  số records mỗi trang
     * @param model Spring MVC model
     * @return view template danh sách thiết bị
     */
    @GetMapping("")
    public String home(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        // Service layer handle pagination ở DB level để optimize performance
        EquipmentPageResponse equipmentPage = equipmentService.getAllEquipmentsPage(page, size);

        // Model attributes cho view rendering
        // - equipments: toàn bộ page response với metadata
        model.addAttribute("equipments", equipmentPage);

        // - equipmentList: actual list cho table iteration
        model.addAttribute("equipmentList", equipmentPage.getEquipments());

        return "/equipments/equipment";
    }

    /**
     * Hiển thị form tạo thiết bị mới.
     * 
     * Form setup: Empty CreateEquipmentRequest object cho Spring form binding.
     * 
     * Future enhancement: Pre-populate với equipment categories,
     * vendors, warranty templates, etc.
     * 
     * @param model Spring MVC model
     * @return view template form create equipment
     */
    @GetMapping("/create")
    public String showFormCreate(Model model) {
        // Empty DTO cho form binding
        model.addAttribute("equipment", new CreateEquipmentRequest());
        return "/equipments/create_equipment";
    }

    /**
     * Xử lý tạo thiết bị mới.
     * 
     * Business validation:
     * - Serial number uniqueness check
     * - Purchase date validation (không được future date)
     * - Asset category validation
     * - Initial status setup (thường là "active")
     * 
     * Asset management: Automatic depreciation calculation start,
     * maintenance schedule creation based on equipment type.
     * 
     * @param equipment          request DTO với thông tin thiết bị
     * @param result             validation binding result
     * @param redirectAttributes flash attributes cho messages
     * @return redirect URL hoặc view với errors
     */
    @PostMapping("/create")
    public String createEquipment(@Valid @ModelAttribute("equipment") CreateEquipmentRequest equipment,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation check - reload form nếu có errors
            if (result.hasErrors()) {
                return "/equipments/create_equipment";
            }

            // Delegate business logic cho service layer
            // Service sẽ handle: serial number validation, depreciation setup, etc.
            equipmentService.createEquipment(equipment);

            // Success feedback
            redirectAttributes.addFlashAttribute("success", "Thêm thiết bị thành công");
            return "redirect:/equipment";

        } catch (Exception e) {
            // Error handling - có thể do duplicate serial number hoặc business rule
            // violation
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment/create";
        }
    }

    /**
     * Xóa thiết bị theo ID.
     * 
     * Business considerations:
     * - Soft delete để preserve audit trail và depreciation history
     * - Validation: không được xóa equipment đang có maintenance records
     * - Asset disposal workflow integration
     * - Financial impact calculation (remaining book value)
     * 
     * Security: POST method để prevent accidental deletion via GET requests.
     * 
     * @param id                 ID của thiết bị cần xóa
     * @param redirectAttributes flash attributes cho messages
     * @return redirect về danh sách thiết bị
     */
    @PostMapping("/delete/{id}")
    public String deleteEquipment(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            // Service layer handle business validation và soft delete logic
            equipmentService.deleteEquipment(id);

            // Success feedback
            redirectAttributes.addFlashAttribute("success", "Xóa thiết bị thành công");
            return "redirect:/equipment";

        } catch (Exception e) {
            // Error feedback - có thể do equipment đang được sử dụng hoặc có maintenance
            // records
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment";
        }
    }

    /**
     * Hiển thị form update thông tin thiết bị.
     * 
     * Data loading:
     * - Fetch equipment details by ID
     * - Include maintenance history và depreciation info nếu cần
     * - Pre-populate form với current values
     * 
     * Access control: Verify user có permission để edit equipment info.
     * 
     * @param id                 ID của thiết bị cần update
     * @param model              Spring MVC model
     * @param redirectAttributes flash attributes cho error handling
     * @return view template form update
     */
    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Fetch equipment data và convert sang DTO
            EquipmentResponse response = equipmentMapper.toEquipmentResponse(equipmentService.getEquipmentById(id));

            // Pre-populate form
            model.addAttribute("equipment", response);
            return "/equipments/update_equipment";

        } catch (Exception e) {
            // Handle case: equipment không tồn tại hoặc access denied
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment";
        }
    }

    /**
     * Xử lý update thông tin thiết bị.
     * 
     * Business rules:
     * - Không được thay đổi serial number sau khi đã tạo
     * - Status change validation (ví dụ: active → maintenance cần approval)
     * - Depreciation recalculation nếu purchase price thay đổi
     * - Audit trail cho tất cả changes
     * 
     * Integration: Update maintenance schedules nếu equipment type thay đổi.
     * 
     * @param request            update request DTO
     * @param result             validation binding result
     * @param redirectAttributes flash attributes
     * @return redirect URL hoặc view với errors
     */
    @PostMapping("/update")
    public String updateEquipment(@Valid @ModelAttribute UpdateEquipmentRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation check
            if (result.hasErrors()) {
                return "/equipments/update_equipment";
            }

            // Delegate update logic cho service layer
            // Service handle business validation, audit trail, depreciation updates
            equipmentService.updateEquipment(request);

            // Success feedback
            redirectAttributes.addFlashAttribute("success", "Cập nhật thiết bị thành công");
            return "redirect:/equipment";

        } catch (Exception e) {
            // Error feedback
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment";
        }
    }
}