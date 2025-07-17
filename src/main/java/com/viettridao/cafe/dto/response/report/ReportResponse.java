package com.viettridao.cafe.dto.response.report;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {

    private LocalDate dateReport;

    private Long revenue;

    private Long expense;
}
