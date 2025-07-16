package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reports.ReportFilterRequest;

import java.util.List;

public interface ReportService {
    byte[] generateReport(ReportFilterRequest request);

    List<?> getReportData(ReportFilterRequest request);
}
