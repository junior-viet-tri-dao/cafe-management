package com.viettridao.cafe.service;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.expenses.BudgetFilterRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.dto.response.expenses.ExpenseRequest;

public interface BudgetService {
    Page<BudgetViewResponse> getBudgetView(BudgetFilterRequest request);
    void addExpense(ExpenseRequest request, String username);
}

