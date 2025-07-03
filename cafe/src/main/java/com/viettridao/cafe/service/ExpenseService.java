package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.expense.ExpenseCreateRequest;
import com.viettridao.cafe.dto.request.expense.ExpenseUpdateRequest;
import com.viettridao.cafe.dto.response.expense.ExpensePageResponse;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;
import com.viettridao.cafe.model.ExpenseEntity;

import java.util.List;

public interface ExpenseService {
    List<ExpenseResponse> getExpenseAll();

    void createExpense(ExpenseCreateRequest request);

    void updateExpense(ExpenseUpdateRequest request);

    void deleteExpense(Integer id);

    ExpenseEntity getExpenseById(Integer id);

    ExpensePageResponse getAllExpense(int page, int size);
}
