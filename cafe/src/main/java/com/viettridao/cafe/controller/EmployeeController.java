package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.employee.CreateEmployeeDTO;
import com.viettridao.cafe.dto.employee.UpdateEmployeeDTO;
import com.viettridao.cafe.service.ChucVuService;
import com.viettridao.cafe.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {
    private final NhanVienService nhanVienService;
    private final ChucVuService chucVuService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("listEmployee", nhanVienService.getAllNhanVien());
        return "/employee/employee";
    }

    @GetMapping("/create")
    public String showFromCreateEmployee(Model model) {
        model.addAttribute("listpositon", chucVuService.getAllChucVu());
        return "/employee/create_employee";
    }

    @PostMapping("/create")
    public String createEmployee(@ModelAttribute CreateEmployeeDTO createEmployeeDTO, RedirectAttributes redirectAttributes) {
        try{
            nhanVienService.createEmployee(createEmployeeDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm nhân viên thành công");
            return "redirect:/employee";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            nhanVienService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("success", "Xoá nhân viên thành công");
            return "redirect:/employee";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee";
        }
    }

    @GetMapping("/edit/{id}")
    public String updateEmployeeForm(@PathVariable("id") Integer id, Model model) {
        UpdateEmployeeDTO dto = nhanVienService.getEmployeeById(id);

        if (dto == null) {
            throw new RuntimeException("Không tìm thấy nhân viên với ID: " + id);
        }
        model.addAttribute("employee", dto);
        model.addAttribute("listpositon", chucVuService.getAllChucVu());
        return "employee/update_employee";
    }

    @PostMapping("/update")
    public String updateEmployee(@ModelAttribute UpdateEmployeeDTO employeeDTO, RedirectAttributes redirectAttributes) {
        try{
            nhanVienService.updateEmployee(employeeDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật nhân viên thành công");
            return "redirect:/employee";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/employee";
        }
    }

}
