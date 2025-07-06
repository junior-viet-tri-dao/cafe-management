package com.viettridao.cafe.service;

import java.time.LocalDate;
import java.util.List;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.response.report.ItemReportResponse;
import com.viettridao.cafe.model.AccountEntity;

public interface ReportService {
    List<ItemReportResponse> getReport(LocalDate fromDate, LocalDate toDate, ReportType type);


}
