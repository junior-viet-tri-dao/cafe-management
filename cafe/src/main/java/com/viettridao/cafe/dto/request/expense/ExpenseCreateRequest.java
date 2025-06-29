package com.viettridao.cafe.dto.request.expense;

import java.time.LocalDate;

import com.viettridao.cafe.model.AccountEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseCreateRequest {

    private Double amount;

    private String expenseName;

    private LocalDate expenseDate;

}
