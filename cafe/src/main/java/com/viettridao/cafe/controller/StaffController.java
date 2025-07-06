package com.viettridao.cafe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StaffController {
    @GetMapping("/list-staff")
    public String list_staff() {
        return "staff/list_staff";
    }

    @GetMapping("/insert-staff")
    public String insert_staff() {
        return "staff/insert_staff";
    }

    @GetMapping("/edit-staff")
    public String edits_taff() {
        return "staff/edit_staff";
    }

    @GetMapping("/delete-staff")
    public String delete_staff() {
        return "staff/delete_staff";
    }

    @GetMapping("/search-staff")
    public String search_staff() {
        return "staff/search_staff";
    }
}
