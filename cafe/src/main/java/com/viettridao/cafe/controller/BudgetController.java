package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.expenses.BudgetFilterRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.dto.response.expenses.ExpenseRequest;
import com.viettridao.cafe.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/budget")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    // 👉 Hiển thị danh sách thu/chi
    @GetMapping("/list")
    public String getBudgetList(@ModelAttribute BudgetFilterRequest filter,
                                Model model,
                                @ModelAttribute("success") String success,
                                @ModelAttribute("error") String error) {
        // ✅ Gán mặc định nếu thiếu ngày
        if (filter.getFromDate() == null || filter.getToDate() == null) {
            LocalDate today = LocalDate.now();
            filter.setToDate(today);
            filter.setFromDate(today.minusDays(7));
        }

        // ✅ Lấy dữ liệu danh sách thu/chi
        Page<BudgetViewResponse> budgetPage = budgetService.getBudgetView(filter);
        model.addAttribute("budgetPage", budgetPage);
        model.addAttribute("filter", filter);

        // ✅ Thêm thông báo nếu có
        if (success != null && !success.isEmpty()) {
            model.addAttribute("success", success);
        }
        if (error != null && !error.isEmpty()) {
            model.addAttribute("error", error);
        }

        return "budget/list";
    }

    // 👉 Hiển thị form thêm chi tiêu
    @GetMapping("/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("expenseRequest")) {
            model.addAttribute("expenseRequest", new ExpenseRequest());
        }
        return "budget/add";
    }

    // 👉 Xử lý thêm chi tiêu
    @PostMapping("/add")
    public String handleAddExpense(@Valid @ModelAttribute("expenseRequest") ExpenseRequest request,
                                   BindingResult result,
                                   RedirectAttributes redirect,
                                   Principal principal) {
        if (result.hasErrors()) {
            redirect.addFlashAttribute("org.springframework.validation.BindingResult.expenseRequest", result);
            redirect.addFlashAttribute("expenseRequest", request);
            redirect.addFlashAttribute("error", "Vui lòng kiểm tra lại thông tin chi tiêu.");
            return "redirect:/budget/add";
        }

        try {
            budgetService.addExpense(request, principal.getName());
            redirect.addFlashAttribute("success", "Thêm chi tiêu thành công.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Đã có lỗi xảy ra khi thêm chi tiêu.");
            return "redirect:/budget/add";
        }

        return "redirect:/budget/list";
    }
}
