package com.viettridao.cafe.dto.request.revenue;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class RevenueFilterRequest {
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;
}
