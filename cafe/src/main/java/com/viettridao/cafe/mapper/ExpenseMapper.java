package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.expenses.ExpenseRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.ExpenseEntity;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

	@Mapping(target = "date", source = "expenseDate")
	@Mapping(target = "expense", source = "amount")
	@Mapping(target = "income", constant = "0.0")
	BudgetViewResponse toDto(ExpenseEntity entity);

	@Mapping(target = "expenseDate", source = "expenseDate")
	@Mapping(target = "expenseName", source = "expenseName")
	@Mapping(target = "amount", source = "amount")
	@Mapping(target = "isDeleted", constant = "false")
	ExpenseEntity fromRequest(ExpenseRequest request);
}
