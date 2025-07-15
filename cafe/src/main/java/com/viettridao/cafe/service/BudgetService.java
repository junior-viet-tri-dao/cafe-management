package com.viettridao.cafe.service;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.expense.BudgetFilterRequest;
import com.viettridao.cafe.dto.request.expense.ExpenseRequest;
import com.viettridao.cafe.dto.response.expense.BudgetResponse;


public interface BudgetService {
    Page<BudgetResponse> getBudgetView(BudgetFilterRequest request);

	void addExpense(ExpenseRequest request, String username);
}
