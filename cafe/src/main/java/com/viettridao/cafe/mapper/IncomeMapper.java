package com.viettridao.cafe.mapper;

import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.InvoiceEntity;

@Component
public class IncomeMapper {

	public BudgetViewResponse fromInvoice(InvoiceEntity entity) {
		BudgetViewResponse dto = new BudgetViewResponse();
		dto.setDate(entity.getCreatedAt().toLocalDate());
		dto.setIncome(entity.getTotalAmount());
		dto.setExpense(0.0); // Đây là khoản thu, không phải chi
		return dto;
	}
}
