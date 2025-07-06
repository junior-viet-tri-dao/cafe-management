package com.viettridao.cafe.service.report;

import com.viettridao.cafe.dto.request.report.ReportFilterRequest;

public interface IReportService {

    byte[] generateReport(ReportFilterRequest request);
}
