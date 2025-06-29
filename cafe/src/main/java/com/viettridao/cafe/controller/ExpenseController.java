package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.expense.ExpenseCreateRequest;
import com.viettridao.cafe.dto.request.expense.ExpenseUpdateRequest;
import com.viettridao.cafe.mapper.ExpenseMapper;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.service.expense.IExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final IExpenseService expenseService;
    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;


    @GetMapping
    public String listExpense(Model model) {
        model.addAttribute("expenses", expenseService.getExpenseAll());
        return "expense/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("expense", new ExpenseCreateRequest());
        return "expense/form-create";
    }

    @PostMapping
    public String createExpense(@ModelAttribute("expense") ExpenseCreateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountEntity account = accountRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản: " + username));
        ExpenseEntity entity = expenseMapper.toEntity(request);
        entity.setAccount(account);
        expenseRepository.save(entity);
        return "redirect:/expense";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        ExpenseUpdateRequest updateRequest = expenseService.getUpdateForm(id);
        model.addAttribute("Expense", updateRequest);
        model.addAttribute("ExpenseId", id);
        model.addAttribute("today", LocalDate.now());
        return "expense/form-edit";
    }

    @PostMapping("/{id}")
    public String updateExpense(@PathVariable Integer id, @ModelAttribute("Expense") ExpenseUpdateRequest request) {
        expenseService.updateExpense(id, request);
        return "redirect:/expense";
    }

    @PostMapping("/delete/{id}")
    public String deleteEquiment(@PathVariable Integer id) {
        expenseService.deleteExpense(id);
        return "redirect:/expense";
    }
}
