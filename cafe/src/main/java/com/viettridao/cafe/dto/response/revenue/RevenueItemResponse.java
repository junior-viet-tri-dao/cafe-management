package com.viettridao.cafe.dto.response.revenue;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class RevenueItemResponse {

    private LocalDate date;

    private Double income;

    private Double expense;
}
