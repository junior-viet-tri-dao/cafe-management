package com.viettridao.cafe.dto.request.budget;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class UpdateExpenseRequest {
    private Integer id;
    private String expenseName;
    private Double amount;
    private LocalDate expenseDate;
}
