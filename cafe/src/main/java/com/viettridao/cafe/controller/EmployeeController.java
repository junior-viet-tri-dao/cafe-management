package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.mapper.EmployeeMapper;
import com.viettridao.cafe.mapper.PositionMapper;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý nhân viên - xử lý full CRUD operations.
 * 
 * Thiết kế architecture:
 * - Follows MVC pattern: Controller → Service → Repository
 * - Validation sử dụng Bean Validation (Jakarta)
 * - Error handling với try-catch và user-friendly messages
 * - POST-redirect-GET pattern để tránh duplicate submissions
 * 
 * Business logic:
 * - Quản lý thông tin nhân viên (tạo, sửa, xóa, xem)
 * - Tích hợp với Position entity cho role assignment
 * - Pagination và search functionality
 * 
 * Security considerations:
 * - Path variable validation để tránh injection
 * - CSRF protection cho POST operations
 * - Input validation với @Valid annotation
 * 
 * @author Cafe Management Team
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {

    // Core dependencies - injected via constructor
    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;
    private final PositionService positionService;
    private final PositionMapper positionMapper;

    /**
     * Hiển thị danh sách nhân viên với tính năng search và pagination.
     * 
     * Features:
     * - Search by keyword (optional)
     * - Pagination với page size configurable
     * - Default page size 10 để optimal performance
     * 
     * Performance: Service layer handle pagination ở DB level để tránh load toàn bộ
     * data.
     * 
     * @param keyword từ khóa tìm kiếm (optional)
     * @param page    số trang hiện tại (default 0)
     * @param size    số record mỗi trang (default 10)
     * @param model   Spring MVC model
     * @return view template danh sách nhân viên
     */
    @GetMapping("")
    public String home(@RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        // Delegate business logic cho service layer
        model.addAttribute("employees", employeeService.getAllEmployees(keyword, page, size));
        return "/employees/employee";
    }

    /**
     * Hiển thị form tạo nhân viên mới.
     * 
     * Prepopulate data:
     * - Dropdown positions list để user select
     * - Empty CreateEmployeeRequest object cho form binding
     * 
     * @param model Spring MVC model
     * @return view template form tạo nhân viên
     */
    @GetMapping("/create")
    public String showFormCreate(Model model) {
        // Load positions cho dropdown selection
        model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPositions()));
        // Empty object cho form binding
        model.addAttribute("employee", new CreateEmployeeRequest());
        return "/employees/create_employee";
    }

    /**
     * Xử lý tạo nhân viên mới.
     * 
     * Validation flow:
     * 1. Bean validation với @Valid annotation
     * 2. BindingResult capture validation errors
     * 3. Nếu có lỗi: reload form với error messages
     * 4. Success: redirect với flash message
     * 
     * Error handling: Catch exceptions và hiển thị user-friendly messages.
     * 
     * @param employee           request DTO với thông tin nhân viên
     * @param result             validation result
     * @param redirectAttributes flash attributes cho redirect
     * @param model              Spring MVC model
     * @return redirect URL hoặc view name
     */
    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute("employee") CreateEmployeeRequest employee,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            // Validation check - nếu có lỗi thì reload form với errors
            if (result.hasErrors()) {
                // Reload positions cho dropdown khi có validation errors
                model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPositions()));
                return "/employees/create_employee";
            }

            // Delegate business logic cho service layer
            employeeService.createEmployee(employee);

            // Success message với flash attributes
            redirectAttributes.addFlashAttribute("success", "Thêm nhân viên thành công");
            return "redirect:/employee";

        } catch (Exception e) {
            // Error handling: catch mọi exceptions và hiển thị user-friendly message
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee/create";
        }
    }

    /**
     * Xóa nhân viên theo ID.
     * 
     * Security: Sử dụng POST method để tránh accidental deletion via GET
     * Path variable validation để đảm bảo ID hợp lệ.
     * 
     * Business rule: Soft delete hoặc hard delete tùy theo business requirement
     * (được implement trong service layer).
     * 
     * @param id                 ID của nhân viên cần xóa
     * @param redirectAttributes flash attributes cho messages
     * @return redirect về danh sách nhân viên
     */
    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            // Delegate business logic cho service layer
            employeeService.deleteEmployee(id);

            // Success feedback
            redirectAttributes.addFlashAttribute("success", "Xóa nhân viên thành công");
            return "redirect:/employee";

        } catch (Exception e) {
            // Error feedback với user-friendly message
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee";
        }
    }

    /**
     * Hiển thị form cập nhật thông tin nhân viên.
     * 
     * Data loading:
     * - Fetch employee data by ID
     * - Load positions cho dropdown update
     * - Pre-populate form với existing data
     * 
     * Error handling: Nếu employee không tồn tại, redirect về list với error
     * message.
     * 
     * @param id                 ID của nhân viên cần update
     * @param model              Spring MVC model
     * @param redirectAttributes flash attributes cho error handling
     * @return view template form update hoặc redirect
     */
    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Fetch employee data và convert sang response DTO
            EmployeeResponse response = employeeMapper.toEmployeeResponse(employeeService.getEmployeeById(id));

            // Load positions cho dropdown selection
            model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPositions()));
            // Pre-populate form với existing data
            model.addAttribute("employee", response);

            return "/employees/update_employee";

        } catch (Exception e) {
            // Error case: employee không tồn tại hoặc access denied
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee";
        }
    }

    /**
     * Xử lý cập nhật thông tin nhân viên.
     * 
     * Note: Validation hiện tại được comment out - có thể enable lại nếu cần.
     * 
     * Update flow:
     * 1. Receive update request DTO
     * 2. (Optional) Validation check
     * 3. Delegate update logic cho service
     * 4. Redirect với success/error message
     * 
     * @param request            update request DTO
     * @param result             validation result (currently not used)
     * @param redirectAttributes flash attributes
     * @param model              Spring MVC model
     * @return redirect URL
     */
    @PostMapping("/update")
    public String updateEmployee(@Valid @ModelAttribute UpdateEmployeeRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            // TODO: Enable validation nếu cần thiết
            // if (result.hasErrors()) {
            // model.addAttribute("positions",
            // positionMapper.toListPositionResponse(positionService.getPositions()));
            // return "/employees/update_employee";
            // }

            // Load positions cho error case fallback
            model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPositions()));

            // Delegate business logic cho service layer
            employeeService.updateEmployee(request);

            // Success feedback
            redirectAttributes.addFlashAttribute("success", "Cập nhật nhân viên thành công");
            return "redirect:/employee";

        } catch (Exception e) {
            // Error feedback
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee";
        }
    }
}
