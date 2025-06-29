package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.budget.CreateExpenseRequest;
import com.viettridao.cafe.dto.request.budget.UpdateExpenseRequest;
import com.viettridao.cafe.dto.response.budget.ExpensePageResponse;
import com.viettridao.cafe.model.ExpenseEntity;

import java.time.LocalDate;

public interface ExpenseService {
    ExpensePageResponse getAllExpenses(String keyword, int page, int size);

    ExpensePageResponse getExpensesByDateRange(LocalDate from, LocalDate to, int page, int size);

    ExpenseEntity createExpense(CreateExpenseRequest request);

    void updateExpense(UpdateExpenseRequest request);

    void deleteExpense(Integer id);

    ExpenseEntity getExpenseById(Integer id);
}
