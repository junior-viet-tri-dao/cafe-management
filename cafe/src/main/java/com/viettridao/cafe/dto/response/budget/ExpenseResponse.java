package com.viettridao.cafe.dto.response.budget;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ExpenseResponse {
    private Integer id;
    private String expenseName;
    private Double amount;
    private LocalDate expenseDate;
    private String accountName;
}
