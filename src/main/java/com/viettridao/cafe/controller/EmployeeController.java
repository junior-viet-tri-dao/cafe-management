package com.viettridao.cafe.controller;

import java.util.List;

import org.aspectj.weaver.Position;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.PositionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final PositionService positionService;

    @GetMapping("/employee")
    public String showEmployee(Model model) {

        List<EmployeeEntity> employees = employeeService.getAllEmployees();

        model.addAttribute("listEmployee", employees);
        return "employees/employee";
    }

    @GetMapping("/employee/create")
    public String showCreateEmployee(Model model) {
        List<PositionEntity> positions = positionService.getAllPositions();

        model.addAttribute("listPosition", positions);
        return "employees/employee-create";
    }

    @GetMapping("/employee/edit/{id}")
    public String showEditEmployee(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            EmployeeEntity employee = employeeService.getEmployeeById(id);

            if (employee == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy nhân viên với ID: " + id);
                return "redirect:/employee";
            }

            model.addAttribute("employee", employee);
            return "employees/employee-edit";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tải thông tin nhân viên");
            return "redirect:/employee";
        }
    }

}
