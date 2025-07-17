package com.viettridao.cafe.service;

import java.time.LocalDate;
import java.util.List;

import com.viettridao.cafe.dto.response.report.ReportResponse;

public interface ReportService {
    List<ReportResponse> getReportByDateRange(LocalDate startDate, LocalDate endDate);
}
