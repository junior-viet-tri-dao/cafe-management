package com.viettridao.cafe.mapper;

import java.time.LocalDate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.InvoiceEntity;

@Mapper(componentModel = "spring")
public interface IncomeMapper {

	@Mapping(target = "date", expression = "java(toLocalDate(entity.getCreatedAt()))")
	@Mapping(target = "income", source = "totalAmount")
	@Mapping(target = "expense", constant = "0.0")
	BudgetViewResponse fromInvoice(InvoiceEntity entity);

	// Helper to convert Timestamp → LocalDate
	default LocalDate toLocalDate(java.time.LocalDateTime datetime) {
		return datetime != null ? datetime.toLocalDate() : null;
	}
}
