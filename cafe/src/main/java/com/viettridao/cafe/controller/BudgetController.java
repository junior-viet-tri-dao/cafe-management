package com.viettridao.cafe.controller;

import java.security.Principal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.expense.BudgetFilterRequest;
import com.viettridao.cafe.dto.request.expense.ExpenseRequest;
import com.viettridao.cafe.dto.response.expense.BudgetResponse;
import com.viettridao.cafe.service.BudgetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    private String formatCurrency(Double value) {
        if (value == null)
            return "0";
        NumberFormat intFormatter = NumberFormat.getIntegerInstance(new Locale("vi", "VN"));
        DecimalFormat decimalFormatter = new DecimalFormat("#,##0.##");
        return (value % 1 == 0) ? intFormatter.format(value) : decimalFormatter.format(value);
    }

    @GetMapping
    public String redirectToBudget(Model model) {
        return "redirect:/budget/budget";
    }

    @GetMapping("/budget")
    public String getBudgetBudget(@ModelAttribute BudgetFilterRequest filter, Model model,
            @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        if (filter.getFromDate() == null || filter.getToDate() == null) {
            LocalDate today = LocalDate.now();
            filter.setToDate(today);
            filter.setFromDate(today.minusDays(7));
        }

        if (filter.getFromDate().isAfter(filter.getToDate())) {
            model.addAttribute("error", "Từ ngày không được lớn hơn đến ngày.");
            model.addAttribute("budgetPage", Page.empty());
            model.addAttribute("filter", filter);
            model.addAttribute("totalIncomeText", "0");
            model.addAttribute("totalExpenseText", "0");
            return "budget/budget";
        }

        Page<BudgetResponse> budgetPage = budgetService.getBudgetView(filter);
        model.addAttribute("budgetPage", budgetPage);
        model.addAttribute("filter", filter);

        double totalIncome = budgetPage.getContent().stream()
                .mapToDouble(item -> item.getIncome() != null ? item.getIncome() : 0.0).sum();
        double totalExpense = budgetPage.getContent().stream()
                .mapToDouble(item -> item.getExpense() != null ? item.getExpense() : 0.0).sum();

        model.addAttribute("totalIncomeText", formatCurrency(totalIncome));
        model.addAttribute("totalExpenseText", formatCurrency(totalExpense));

        if (success != null && !success.isEmpty()) {
            model.addAttribute("success", success);
        }
        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", error);
        }

        return "budget/budget";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("expenseRequest")) {
            model.addAttribute("expenseRequest", new ExpenseRequest());
        }
        return "budget/create_budget";
    }

    @PostMapping("/create")
    public String handleCreateExpense(@Valid @ModelAttribute("expenseRequest") ExpenseRequest request,
            BindingResult result, RedirectAttributes redirect, Principal principal) {

        if (result.hasErrors()) {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.expenseRequest", result);
            redirect.addFlashAttribute("expenseRequest", request);
            redirect.addFlashAttribute("error", "Vui lòng kiểm tra lại thông tin chi tiêu.");
            return "redirect:/budget/create";
        }

        if (principal == null) {
            redirect.addFlashAttribute("error", "Người dùng chưa đăng nhập.");
            return "redirect:/budget/create";
        }

        try {
            budgetService.addExpense(request, principal.getName());
            redirect.addFlashAttribute("success", "Thêm chi tiêu thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Đã có lỗi xảy ra khi thêm chi tiêu.");
            return "redirect:/budget/create";
        }

        return "redirect:/budget/budget";
    }
}