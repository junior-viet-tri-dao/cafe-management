package com.viettridao.cafe.controller;

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

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.mapper.EmployeeMapper;
import com.viettridao.cafe.mapper.PositionMapper;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.PositionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    // Hiển thị form tạo nhân viên mới
    // - Nạp danh sách chức vụ cho dropdown
    // - Tạo mới đối tượng CreateEmployeeRequest để binding với form
    @GetMapping("/create")
    public String showFormCreate(Model model) {
        // Nạp danh sách chức vụ cho dropdown
        model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPositions()));
        // Tạo mới đối tượng cho binding form
        model.addAttribute("employee", new CreateEmployeeRequest());
        return "/employees/create_employee";
    }

    // Xử lý submit form tạo nhân viên mới
    // - Validate dữ liệu đầu vào bằng @Valid và BindingResult
    // - Nếu có lỗi: nạp lại danh sách chức vụ, trả về form và hiển thị lỗi
    // - Nếu hợp lệ: gọi service lưu nhân viên, thêm thông báo thành công, chuyển
    // hướng về danh sách
    // - Nếu có lỗi hệ thống: hiển thị thông báo lỗi, chuyển hướng về lại form tạo
    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute("employee") CreateEmployeeRequest employee,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            // Nếu có lỗi validate, trả về lại form và hiển thị lỗi
            if (result.hasErrors()) {
                // Nạp lại danh sách chức vụ cho dropdown
                model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPositions()));
                return "/employees/create_employee";
            }

            // Kiểm tra xem username đã tồn tại chưa
            if (employeeService.existsByUsername(employee.getUsername())) {
                // Nếu đã tồn tại, thêm lỗi vào BindingResult và trả về form
                result.rejectValue("username", "error.username", "Tên đăng nhập đã tồn tại");
                model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPositions()));
                return "/employees/create_employee";
            }

            // Nếu hợp lệ, gọi service để lưu nhân viên
            employeeService.createEmployee(employee);

            // Thêm thông báo thành công, chuyển hướng về danh sách nhân viên
            redirectAttributes.addFlashAttribute("success", "Thêm nhân viên thành công");
            return "redirect:/employee";

        } catch (Exception e) {
            // Nếu có lỗi hệ thống hoặc nghiệp vụ, hiển thị thông báo lỗi, chuyển hướng về
            // lại form tạo
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
     * Update flow:
     * 1. Receive update request DTO
     * 2. (Optional) Validation check
     * 3. Delegate update logic cho service
     * 4. Redirect với success/error message
     * 
     * @param request            update request DTO
     * @param result             validation result
     * @param redirectAttributes flash attributes
     * @param model              Spring MVC model
     * @return redirect URL
     */
    @PostMapping("/update")
    public String updateEmployee(@Valid @ModelAttribute("employee") UpdateEmployeeRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        try {
            // Nếu có lỗi validate, trả về lại form và hiển thị lỗi
            if (result.hasErrors()) {
                // Nạp lại danh sách chức vụ cho dropdown
                model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPositions()));
                return "/employees/update_employee";
            }

            // Gọi service để cập nhật nhân viên
            employeeService.updateEmployee(request);

            // Thêm thông báo thành công, chuyển hướng về danh sách nhân viên
            redirectAttributes.addFlashAttribute("success", "Cập nhật nhân viên thành công");
            return "redirect:/employee";

        } catch (Exception e) {
            // Nếu có lỗi hệ thống hoặc nghiệp vụ, hiển thị thông báo lỗi, chuyển hướng về
            // lại form cập nhật
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee";
        }
    }
}
