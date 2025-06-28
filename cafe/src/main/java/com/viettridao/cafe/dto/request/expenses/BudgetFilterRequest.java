package com.viettridao.cafe.dto.request.expenses;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BudgetFilterRequest {
    private LocalDate fromDate;
    private LocalDate toDate;
    private int page = 0;
    private int size = 10;
}
