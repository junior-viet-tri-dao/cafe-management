package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.budget.ExpenseResponse;
import com.viettridao.cafe.model.ExpenseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {
    public ExpenseResponse toExpenseResponse(ExpenseEntity entity) {
        if (entity == null)
            return null;
        ExpenseResponse response = new ExpenseResponse();
        response.setId(entity.getId());
        response.setExpenseName(entity.getExpenseName());
        response.setAmount(entity.getAmount());
        response.setExpenseDate(entity.getExpenseDate());
        response.setAccountName(entity.getAccount() != null ? entity.getAccount().getUsername() : null);
        return response;
    }
}
