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

import com.viettridao.cafe.dto.request.expenses.BudgetFilterRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.dto.response.expenses.ExpenseRequest;
import com.viettridao.cafe.service.BudgetService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
        if (filter.getFromDate() == null || filter.getToDate() == null) {
            LocalDate today = LocalDate.now();
            filter.setToDate(today);
            filter.setFromDate(today.minusDays(7));
        }

        Page<BudgetViewResponse> budgetPage = budgetService.getBudgetView(filter);
        model.addAttribute("budgetPage", budgetPage);
        model.addAttribute("filter", filter);

        // ✅ Tính tổng
        double totalIncome = budgetPage.getContent().stream()
                .mapToDouble(item -> item.getIncome() != null ? item.getIncome() : 0.0)
                .sum();
        double totalExpense = budgetPage.getContent().stream()
                .mapToDouble(item -> item.getExpense() != null ? item.getExpense() : 0.0)
                .sum();

        NumberFormat intFormatter = NumberFormat.getIntegerInstance(new Locale("vi", "VN"));
        DecimalFormat decimalFormatter = new DecimalFormat("#,##0.##");

        String totalIncomeText = (totalIncome % 1 == 0)
                ? intFormatter.format(totalIncome)
                : decimalFormatter.format(totalIncome);

        String totalExpenseText = (totalExpense % 1 == 0)
                ? intFormatter.format(totalExpense)
                : decimalFormatter.format(totalExpense);

        model.addAttribute("totalIncomeText", totalIncomeText);
        model.addAttribute("totalExpenseText", totalExpenseText);

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
