package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.response.report.ReportItemResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate, String category);

    long getTongThu(List<ReportItemResponse> reportList);

    long getTongChi(List<ReportItemResponse> reportList);
}
