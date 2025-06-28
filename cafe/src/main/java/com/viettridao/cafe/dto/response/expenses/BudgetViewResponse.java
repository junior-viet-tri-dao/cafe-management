package com.viettridao.cafe.dto.response.expenses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BudgetViewResponse {
    private LocalDate date;
    private Double income;   
    private Double expense;  
}

