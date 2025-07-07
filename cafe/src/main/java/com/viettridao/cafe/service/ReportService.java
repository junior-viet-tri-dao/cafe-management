package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reports.ReportFilterRequest;

public interface ReportService {
    byte[] generateReport(ReportFilterRequest request);
}
