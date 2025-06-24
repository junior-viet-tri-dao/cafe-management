package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EmployeeController {
    @GetMapping("/employee")
    public String showEmployee() {
        return "employees/employee";
    }

    @GetMapping("/employee/create")
    public String showCreateEmployee() {
        return "employees/employee-create";
    }

}
