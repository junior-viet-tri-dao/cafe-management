package com.viettridao.cafe.dto.response.expense;

import com.viettridao.cafe.dto.response.account.AccountResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
