package com.viettridao.cafe.mapper;

import java.time.LocalDateTime;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.InvoiceEntity;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

	@Mapping(target = "date", expression = "java(convertToDate(entity.getCreatedAt()))")
	@Mapping(target = "income", source = "totalAmount")
	@Mapping(target = "expense", constant = "0.0")
	BudgetViewResponse toBudgetResponse(InvoiceEntity entity);

	// Helper method to convert LocalDateTime → LocalDate
	default java.time.LocalDate convertToDate(LocalDateTime datetime) {
		return datetime != null ? datetime.toLocalDate() : null;
	}
}
