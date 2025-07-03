package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.expense.ExpenseCreateRequest;
import com.viettridao.cafe.mapper.ExpenseMapper;
import com.viettridao.cafe.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;

    @GetMapping
    public String listExpense(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              Model model) {
        model.addAttribute("expenses", expenseService.getAllExpense(page, size));
        return "/expenses/expense";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("expense", new ExpenseCreateRequest());
        return "/expenses/create_expense";
    }

    @PostMapping
    public String createExpense(@Valid @ModelAttribute("expense") ExpenseCreateRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/expenses/create_expense";
            }
            expenseService.createExpense(request);
            redirectAttributes.addFlashAttribute("success", "Thêm chi tiêu thành công");
            return "redirect:/expense";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/expense/new";
        }
    }

//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Integer id, Model model) {
//        ExpenseResponse expense = expenseMapper.toResponse(expenseService.getExpenseById(id));
//        model.addAttribute("Expense", expense);
//        model.addAttribute("ExpenseId", id);
//        model.addAttribute("today", LocalDate.now());
//        return "expense/form-edit";
//    }
//
//    @PostMapping("/{id}")
//    public String updateExpense(@PathVariable Integer id, @ModelAttribute("Expense") ExpenseUpdateRequest request) {
//        expenseService.updateExpense(request);
//        return "redirect:/expense";
//    }
//
//    @PostMapping("/delete/{id}")
//    public String deleteEquiment(@PathVariable Integer id) {
//        expenseService.deleteExpense(id);
//        return "redirect:/expense";
//    }

}
