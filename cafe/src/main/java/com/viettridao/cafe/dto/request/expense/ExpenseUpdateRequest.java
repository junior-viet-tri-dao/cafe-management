package com.viettridao.cafe.dto.request.expense;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpenseUpdateRequest {

    private Integer id;

    private Double amount;

    private String expenseName;

    private LocalDate expenseDate;

    private Integer accountId;

}
