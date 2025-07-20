package com.viettridao.cafe.dto.request.revenue;

import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RevenueFilterRequest {
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Ngày bắt đầu không được vượt quá ngày hiện tại")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @PastOrPresent(message = "Ngày kết thúc không được vượt quá ngày hiện tại")
    private LocalDate endDate;
}