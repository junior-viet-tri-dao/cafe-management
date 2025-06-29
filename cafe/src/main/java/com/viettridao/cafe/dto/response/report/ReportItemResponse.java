package com.viettridao.cafe.dto.response.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemResponse {
    private LocalDate ngay;
    private long thu;
    private long chi;
}
