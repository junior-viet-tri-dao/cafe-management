package com.viettridao.cafe.mapper;

import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.dto.response.expense.BudgetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface IncomeMapper {

    @Mapping(target = "date", expression = "java(toLocalDate(entity.getCreatedAt()))")
    @Mapping(target = "income", source = "totalAmount")
    @Mapping(target = "expense", constant = "0.0")
    BudgetResponse fromInvoice(InvoiceEntity entity);

    // Helper to convert Timestamp → LocalDate
    default LocalDate toLocalDate(java.time.LocalDateTime datetime) {
        return datetime != null ? datetime.toLocalDate() : null;
    }
}
