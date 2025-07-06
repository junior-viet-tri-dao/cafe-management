package com.viettridao.cafe.dto.request.revenue;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevenueFilterRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Từ ngày không được vượt quá ngày hiện tại")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Đến ngày không được vượt quá ngày hiện tại")
    private LocalDate endDate;

    @AssertTrue(message = "Từ ngày phải nhỏ hơn hoặc bằng đến ngày")
    public boolean isValidRange() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }
}
