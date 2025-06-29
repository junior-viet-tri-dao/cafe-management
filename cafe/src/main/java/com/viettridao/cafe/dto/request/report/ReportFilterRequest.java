package com.viettridao.cafe.dto.request.report;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReportFilterRequest {
    private String category;

    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}$", message = "Ngày không hợp lệ")
    private String fromDate;

    @Pattern(regexp = "^$|\\d{4}-\\d{2}-\\d{2}$", message = "Ngày không hợp lệ")
    private String toDate;
}
