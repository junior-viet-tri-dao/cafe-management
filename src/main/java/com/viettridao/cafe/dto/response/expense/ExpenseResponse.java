package com.viettridao.cafe.dto.response.expense;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private Integer id;
    private Double amount;
    private String expenseName;
    private LocalDate expenseDate;
    private Boolean isDeleted;
}
