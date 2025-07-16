package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.dto.request.reports.ReportFilterRequest;
import com.viettridao.cafe.dto.request.reservation.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;
import com.viettridao.cafe.repository.*;
import com.viettridao.cafe.service.ReportService;
import com.viettridao.cafe.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ImportRepository importRepository;
    private final ExportRepository exportRepository;
    private final InvoiceRepository invoiceRepository;
    private final EmployeeRepository employeeRepository;
    private final ExpenseRepository expenseRepository;
    private final RevenueService revenueService;

    @Override
    public byte[] generateReport(ReportFilterRequest request) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }

        List<?> reportData = switch (request.getType()) {
            case IMPORT_ONLY -> importRepository.findByImportDateBetweenAndIsDeletedFalse(start, end);
            case EXPORT_ONLY -> exportRepository.findAllByIsDeletedFalseAndExportDateBetween(start,end);
            case IMPORT_EXPORT -> {
                Map<String, Object> map = new HashMap<>();
                map.put("imports", importRepository.findByImportDateBetweenAndIsDeletedFalse(start, end));
                map.put("exports", exportRepository.findAllByIsDeletedFalseAndExportDateBetween(start,end));
                yield List.of(map);
            }
            case REVENUE_SUMMARY -> {
                RevenueFilterRequest revenueRequest = new RevenueFilterRequest(start,end);
                RevenueResponse revenue = revenueService.getRevenueSummary(revenueRequest);
                yield List.of(revenue);
            }
            case EMPLOYEE_SALARY -> employeeRepository.findEmployeeByIsDeletedFalse();
            case EXPENSE_ONLY -> expenseRepository.findByExpenseDateBetweenAndIsDeletedFalse(start, end);
            case INVOICE_MONTHLY -> invoiceRepository.findByCreatedAtBetweenAndIsDeletedFalse(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        };

        return PdfExportServiceImpl.generatePdf(reportData, request.getType());
    }

    public List<?> getReportData(ReportFilterRequest request) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }

        return switch (request.getType()) {
            case IMPORT_ONLY -> importRepository.findByImportDateBetweenAndIsDeletedFalse(start, end);
            case EXPORT_ONLY -> exportRepository.findAllByIsDeletedFalseAndExportDateBetween(start, end);
            case IMPORT_EXPORT -> {
                Map<String, Object> map = new HashMap<>();
                map.put("imports", importRepository.findByImportDateBetweenAndIsDeletedFalse(start, end));
                map.put("exports", exportRepository.findAllByIsDeletedFalseAndExportDateBetween(start, end));
                yield List.of(map);
            }
            case REVENUE_SUMMARY -> {
                RevenueFilterRequest revenueRequest = new RevenueFilterRequest(start, end);
                RevenueResponse revenue = revenueService.getRevenueSummary(revenueRequest);
                yield List.of(revenue);
            }
            case EMPLOYEE_SALARY -> employeeRepository.findEmployeeByIsDeletedFalse();
            case EXPENSE_ONLY -> expenseRepository.findByExpenseDateBetweenAndIsDeletedFalse(start, end);
            case INVOICE_MONTHLY -> invoiceRepository.findByCreatedAtBetweenAndIsDeletedFalseAndStatusEquals(start.atStartOfDay(), end.plusDays(1).atStartOfDay(), InvoiceStatus.PAID);
        };
    }
}
