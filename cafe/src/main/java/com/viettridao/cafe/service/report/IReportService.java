package com.viettridao.cafe.service.report;

import com.viettridao.cafe.dto.request.report.ReportFilterRequest;

import java.util.List;

public interface IReportService {

    byte[] generateReport(ReportFilterRequest request);

    List<?> getReportData(ReportFilterRequest request);
}
