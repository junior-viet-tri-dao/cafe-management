package com.viettridao.cafe.dto.request.budget;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CreateExpenseRequest {
    private String expenseName;
    private Double amount;
    private LocalDate expenseDate;
}
