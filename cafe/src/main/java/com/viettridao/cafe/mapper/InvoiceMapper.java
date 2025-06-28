package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.InvoiceEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {

	private final ModelMapper modelMapper;

	public BudgetViewResponse toBudgetResponse(InvoiceEntity entity) {
		BudgetViewResponse dto = new BudgetViewResponse();
		dto.setDate(entity.getCreatedAt().toLocalDate());
		dto.setIncome(entity.getTotalAmount());
		dto.setExpense(0.0);
		return dto;
	}
}
