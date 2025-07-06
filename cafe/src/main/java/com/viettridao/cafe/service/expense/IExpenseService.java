package com.viettridao.cafe.service.expense;

import com.viettridao.cafe.dto.request.expense.ExpenseCreateRequest;
import com.viettridao.cafe.dto.request.expense.ExpenseUpdateRequest;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;

import java.util.List;

public interface IExpenseService {

    List<ExpenseResponse> getExpenseAll();

    void createExpense(ExpenseCreateRequest request);

    ExpenseUpdateRequest getUpdateForm(Integer id);

    void updateExpense(Integer id, ExpenseUpdateRequest request);

    void deleteExpense(Integer id);
}
