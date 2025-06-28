package com.viettridao.cafe.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.dto.request.expenses.BudgetFilterRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.dto.response.expenses.ExpenseRequest;
import com.viettridao.cafe.service.BudgetService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/budget")
@RequiredArgsConstructor
public class BudgetController {

	private final BudgetService budgetService;

	// ✅ Hiển thị danh sách thu - chi
	@GetMapping("/list")
	public String showBudgetList(@ModelAttribute BudgetFilterRequest request, Model model) {
		// Nếu không nhập ngày thì mặc định 7 ngày gần nhất
		if (request.getFromDate() == null || request.getToDate() == null) {
			LocalDate today = LocalDate.now();
			request.setToDate(today);
			request.setFromDate(today.minusDays(6));
		}

		// Nếu người dùng chọn ngày sai (from > to), tự động chỉnh lại
		if (request.getFromDate().isAfter(request.getToDate())) {
			request.setFromDate(request.getToDate().minusDays(6));
		}

		Page<BudgetViewResponse> page = budgetService.getBudgetView(request);

		double totalIncome = page.stream().mapToDouble(b -> b.getIncome() != null ? b.getIncome() : 0.0).sum();

		double totalExpense = page.stream().mapToDouble(b -> b.getExpense() != null ? b.getExpense() : 0.0).sum();

		model.addAttribute("budgetPage", page);
		model.addAttribute("filter", request);
		model.addAttribute("totalIncome", totalIncome);
		model.addAttribute("totalExpense", totalExpense);

		return "budget/list";
	}

	// ✅ Form thêm chi tiêu
	@GetMapping("/add")
	public String showAddExpenseForm(Model model) {
		model.addAttribute("expense", new ExpenseRequest());
		return "budget/add";
	}

	// ✅ Xử lý thêm chi tiêu
	@PostMapping("/add")
	public String addExpense(@ModelAttribute("expense") ExpenseRequest request, Authentication authentication) {
		String username = authentication.getName();
		budgetService.addExpense(request, username);
		return "redirect:/budget/list";
	}
}
