package com.viettridao.cafe.dto.request.report;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import com.viettridao.cafe.common.ReportType;

@Getter
@Setter
public class ReportFilterRequest {

    @PastOrPresent(message = "Từ ngày không được vượt quá ngày hiện tại")
    private LocalDate startDate;

    @PastOrPresent(message = "Đến ngày không được vượt quá ngày hiện tại")
    private LocalDate endDate;


    private ReportType type;

    @AssertTrue(message = "Từ ngày phải nhỏ hơn hoặc bằng đến ngày")
    public boolean isValidDateRange() {
        if (startDate != null && endDate != null) {
            return !startDate.isAfter(endDate);
        }
        return true;
    }
}
