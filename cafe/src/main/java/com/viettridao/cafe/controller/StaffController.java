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
            @RequestParam(required = false, defaultValue = "10") int size, Model model) {

        model.addAttribute("employees", employeeService.getAllEmployees(keyword, page, size));

        return "staff/list_staff";
    }

    // Tạo nhân viên
    @GetMapping("/insert")
    public String show_page_insert_staff(Model model) {
        model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPosition()));
        model.addAttribute("employee", new CreateEmployeeRequest());
        return "staff/insert_staff";
    }

    // Tạo nhân viên
    @PostMapping("/insert")
    public String createEmployee(@Valid @ModelAttribute("employee") CreateEmployeeRequest employee, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model) {
        try{
            if (result.hasErrors()) {
                model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPosition()));
                return "staff/insert_staff";
            }

            employeeService.createEmployee(employee);
            redirectAttributes.addFlashAttribute("success", "Thêm nhân viên thành công");
            return "staff/insert_staff";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "staff/insert_staff";
        }
    }


    // hiện form Chỉnh sửa nhân viên
    @GetMapping("/edit/{id}")
    public String show_form_update(@PathVariable("id") Integer id, Model model) {
        EmployeeEntity employeeEntity = employeeService.getEmployeeById(id);
        model.addAttribute("positions", positionMapper.toListPositionResponse(positionService.getPosition()));
        model.addAttribute("employee", employeeMapper.toEmployeeRespones(employeeEntity));
        return "staff/edit_staff";
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
    public String show_form_delete_staff(@PathVariable("id") Integer id) {
        employeeService.deleteEmployee(id);
        return "redirect:/staff";
    }

//    //  xoá nhân viên
//    @GetMapping("/delete")
//    public String deleteStaffById(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
//
//        if(!employeeService.deleteEmployee(id)){
//            throw new RuntimeException("Không xoá được nhân viên");
//        }
//
//        return "staff/delete_staff";
//    }

    // tìm kiếm nhân viên
    @GetMapping("/search")
    public String search_staff() {
        return "staff/search_staff";
    }
}
