package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.dto.response.expenses.ExpenseRequest;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.ExpenseEntity;

@Component
public class ExpenseMapper extends BaseMapper<ExpenseEntity, ExpenseRequest, BudgetViewResponse> {

    public ExpenseMapper(ModelMapper modelMapper) {
        super(modelMapper, ExpenseEntity.class, ExpenseRequest.class, BudgetViewResponse.class);
    }

    @Override
    public BudgetViewResponse toDto(ExpenseEntity entity) {
        BudgetViewResponse dto = new BudgetViewResponse();
        dto.setDate(entity.getExpenseDate());
        dto.setExpense(entity.getAmount());
        dto.setIncome(0.0); // Vì đây là chi tiêu
        return dto;
    }

    @Override
    public ExpenseEntity fromRequest(ExpenseRequest request) {
        ExpenseEntity entity = new ExpenseEntity();
        entity.setExpenseDate(request.getExpenseDate());
        entity.setExpenseName(request.getExpenseName());
        entity.setAmount(request.getAmount());
        entity.setIsDeleted(false);
        return entity;
    }
}

