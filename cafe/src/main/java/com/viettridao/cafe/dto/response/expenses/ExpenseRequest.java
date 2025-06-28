package com.viettridao.cafe.dto.response.expenses;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseRequest {
    private String expenseName;
    private Double amount;
    private LocalDate expenseDate;
}
