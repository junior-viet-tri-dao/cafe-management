package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExpenseController {
    @GetMapping("/expense")
    public String showExpense() {
        return "expenses/expense";
    }

    @GetMapping("/expense/create")
    public String showCreateExpense() {
        return "expenses/expense-create";
    }
}
