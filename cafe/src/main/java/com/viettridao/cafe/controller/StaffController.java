package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.mapper.EmployeeMapper;
import com.viettridao.cafe.mapper.PositionMapper;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping("/staff")
public class StaffController {
    private final EmployeeService employeeService;
    private final PositionService positionService;
    private final PositionMapper positionMapper;
    private  final EmployeeMapper employeeMapper;
    // Hiểu thị danh sách nhân viên
    @GetMapping("")
    public String list_staff(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            Model model,
            RedirectAttributes redirectAttributes) {

        // 1. Validate các tham số page và size
        if (page < 0) {
            page = 0; // Đảm bảo số trang không âm
        }
        if (size <= 0) {
            size = 10; // Đảm bảo kích thước trang tối thiểu
        }

        try {
            // 2. Lấy dữ liệu từ service
            model.addAttribute("employees", employeeService.getAllEmployees(keyword, page, size));

            // Trả về tên view
            return "staff/list_staff";
        } catch (Exception e) {
            // 4. Xử lý lỗi nếu việc truy vấn dữ liệu thất bại
            // logger.error("Lỗi khi lấy danh sách nhân viên: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi tải danh sách nhân viên. Vui lòng thử lại sau.");
            // Chuyển hướng về trang danh sách nhân viên để hiển thị lỗi
            return "redirect:/staff";
        }
    }

    // Tạo nhân viên
    @GetMapping("/insert")
    public String show_page_insert_staff(Model model, RedirectAttributes redirectAttributes) {
        try {
            // Lấy danh sách chức vụ
            model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPosition()));

            // Tạo một đối tượng trống để liên kết với form
            model.addAttribute("employee", new CreateEmployeeRequest());

            // Trả về tên view
            return "staff/insert_staff";

        } catch (Exception e) {
            // Xử lý lỗi nếu không thể tải danh sách chức vụ từ cơ sở dữ liệu
            // logger.error("Lỗi khi tải danh sách chức vụ: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể tải trang thêm nhân viên. Vui lòng thử lại sau.");
            return "redirect:/staff";
        }
    }

    // Tạo nhân viên
    @PostMapping("/insert")
    public String createEmployee(@Valid @ModelAttribute("employee") CreateEmployeeRequest employee,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        // 1. Kiểm tra kết quả validation từ @Valid
        if (result.hasErrors()) {
            // Có lỗi validation, giữ lại các thông báo lỗi và quay lại form
            // Cần thêm lại danh sách chức vụ vào model để form có thể hiển thị
            model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPosition()));
            return "staff/insert_staff";
        }

        try {
            // 2. Thực thi logic nghiệp vụ (tạo nhân viên)
            employeeService.createEmployee(employee);

            // 3. Thông báo thành công và chuyển hướng
            redirectAttributes.addFlashAttribute("successMessage", "Thêm nhân viên thành công.");
            return "redirect:/staff";

        } catch (DataIntegrityViolationException e) {
            // 4. Xử lý lỗi cơ sở dữ liệu: ví dụ, tên tài khoản đã tồn tại
            // Ghi log lỗi để dễ dàng gỡ lỗi
            // logger.error("Lỗi dữ liệu khi tạo nhân viên: ", e);

            // Thêm lại danh sách chức vụ vào model
            model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPosition()));

            // Thêm lỗi vào BindingResult để hiển thị trên form
            result.rejectValue("account.accountName", "error.employee", "Tên tài khoản đã tồn tại.");

            // Quay lại form
            return "staff/insert_staff";
        } catch (Exception e) {
            // 5. Xử lý các lỗi chung khác
            // Ghi log lỗi
            // logger.error("Lỗi khi tạo nhân viên: ", e);

            // Thêm lại danh sách chức vụ vào model
            model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPosition()));

            // Thêm lỗi chung vào BindingResult để hiển thị trên form
            result.reject("error.employee", "Đã xảy ra lỗi không xác định khi thêm nhân viên.");

            // Quay lại form
            return "staff/insert_staff";
        }
    }


    // hiện form Chỉnh sửa nhân viên
    @GetMapping("/edit/{id}")
    public String show_form_update(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        if (id == null || id <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "ID nhân viên không hợp lệ.");
            return "redirect:/staff";
        }


        try {
            // 2. Lấy dữ liệu từ service
            EmployeeEntity employeeEntity = employeeService.getEmployeeById(id);

            // 3. Nếu không tìm thấy nhân viên, ném ngoại lệ
            if (employeeEntity == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy nhân viên cần chỉnh sửa.");
                return "redirect:/staff";
            }

            // 4. Thêm các đối tượng vào Model để hiển thị trên form
            model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPosition()));
            model.addAttribute("employee", employeeMapper.toEmployeeRespones(employeeEntity));

            // 5. Trả về tên view
            return "staff/edit_staff";

        } catch (Exception e) {
            // 6. Xử lý các lỗi khác, ví dụ: lỗi cơ sở dữ liệu
            // logger.error("Lỗi khi truy xuất dữ liệu nhân viên: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi tải dữ liệu nhân viên.");
            return "redirect:/staff";
        }
    }


    // nhận thông tin chỉnh sửa nhân viên
    @PostMapping("/edit")
    public String show_form_update(@Valid @ModelAttribute UpdateEmployeeRequest request, BindingResult result, RedirectAttributes redirectAttributes) {

        try{
            if(result.hasErrors()){
                return  "staff/edit_staff";

            }
            else {
                employeeService.updateEmployee(request);
                redirectAttributes.addFlashAttribute("Success", "Chỉnh sửa nhân viên thành công");
                return "redirect:/staff";
            }

        } catch (Exception e) {
            throw new RuntimeException("Không update được nhân viên");
        }
    }

    // hiện form xoá nhân viên
    @GetMapping("/delete/{id}")
    public String show_form_delete_staff(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        // 1. Validation (kiểm tra) ID
        if (id == null || id <= 0) {
            // Trả về một thông báo lỗi nếu ID không hợp lệ
            // (Đây là trường hợp hiếm, nhưng nên xử lý)
            redirectAttributes.addFlashAttribute("errorMessage", "ID nhân viên không hợp lệ.");
            return "redirect:/staff";
        }
        try {
            // 2. Logic xóa
            employeeService.deleteEmployee(id);
            // 3. Thông báo thành công
            redirectAttributes.addFlashAttribute("successMessage", "Xóa nhân viên thành công.");
        } catch (Exception e) {
            // 4. Xử lý lỗi nếu việc xóa thất bại
            // (ví dụ: nhân viên không tồn tại, có lỗi liên quan đến ràng buộc khóa ngoại)
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa nhân viên này. Vui lòng kiểm tra lại.");
        }
        return "redirect:/staff";
    }




}
