package com.viettridao.cafe.service;

import java.time.LocalDate;
import java.util.List;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;

public interface ReportService {
	List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate, ReportType type);

	List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate);

}
