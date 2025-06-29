package com.viettridao.cafe.dto.response.expense;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.response.account.AccountResponse;

@Getter
@Setter
public class ExpenseResponse {

    private Integer id;

    private Double amount;

    private String expenseName;

    private LocalDate expenseDate;

    private Boolean deleted;

    private AccountResponse account;

}
