
package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.budget.CreateExpenseRequest;
import com.viettridao.cafe.dto.request.budget.UpdateExpenseRequest;
import com.viettridao.cafe.dto.response.budget.ExpensePageResponse;
import com.viettridao.cafe.dto.response.budget.ExpenseResponse;
import com.viettridao.cafe.mapper.ExpenseMapper;
import com.viettridao.cafe.model.ExpenseEntity;
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
@RequestMapping("/budget")
public class BudgetController {
    private final ExpenseService expenseService;
    private final ExpenseMapper expenseMapper;

    @GetMapping("")
    public String home(@RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        ExpensePageResponse expenses = expenseService.getAllExpenses(keyword, page, size);
        model.addAttribute("expenses", expenses);
        return "/budgets/budget";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("expense", new CreateExpenseRequest());
        return "/budgets/create_budget";
    }

    @PostMapping("/create")
    public String createExpense(@Valid @ModelAttribute("expense") CreateExpenseRequest request, BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "/budgets/create_budget";
            }
            expenseService.createExpense(request);
            redirectAttributes.addFlashAttribute("success", "Thêm khoản chi thành công");
            return "redirect:/budget";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/budget/create";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            expenseService.deleteExpense(id);
            redirectAttributes.addFlashAttribute("success", "Xoá khoản chi thành công");
            return "redirect:/budget";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/budget";
        }
    }

    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ExpenseResponse response = expenseMapper.toExpenseResponse(expenseService.getExpenseById(id));
            model.addAttribute("expense", response);
            return "/budgets/update_budget";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/budget";
        }
    }

    @PostMapping("/update")
    public String updateExpense(@Valid @ModelAttribute UpdateExpenseRequest request, BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "/budgets/update_budget";
            }
            expenseService.updateExpense(request);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khoản chi thành công");
            return "redirect:/budget";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/budget";
        }
    }
}
