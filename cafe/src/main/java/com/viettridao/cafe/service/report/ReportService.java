package com.viettridao.cafe.service.report;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viettridao.cafe.dto.request.report.ReportFilterRequest;
import com.viettridao.cafe.dto.request.revenue.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;
import com.viettridao.cafe.repository.*;
import com.viettridao.cafe.service.pdf.PdfExportService;
import com.viettridao.cafe.service.revenue.IRevenueService;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {


        private final ImportRepository importRepository;
        private final ExportRepository exportRepository;
        private final InvoiceRepository invoiceRepository;
        private final EmployeeRepository employeeRepository;
        private final ExpenseRepository expenseRepository;
        private final IRevenueService revenueService;
        private final PdfExportService pdfExportService;

        @Override
        public byte[] generateReport(ReportFilterRequest request) {
            LocalDate start = request.getStartDate();
            LocalDate end = request.getEndDate();

            if (start == null || end == null || start.isAfter(end)) {
                throw new IllegalArgumentException("Ngày không hợp lệ");
            }

            List<?> reportData = switch (request.getType()) {
                case IMPORT_ONLY -> importRepository.findByImportDateBetweenAndDeletedFalse(start, end);
                case EXPORT_ONLY -> exportRepository.findAllByDeletedFalseAndExportDateBetween(start,end);
                case IMPORT_EXPORT -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("imports", importRepository.findByImportDateBetweenAndDeletedFalse(start, end));
                    map.put("exports", exportRepository.findAllByDeletedFalseAndExportDateBetween(start,end));
                    yield List.of(map);
                }
                case REVENUE_SUMMARY -> {
                    RevenueFilterRequest revenueRequest = new RevenueFilterRequest(start,end);
                    RevenueResponse revenue = revenueService.getRevenueSummary(revenueRequest);
                    yield List.of(revenue);
                }
                case EMPLOYEE_SALARY -> employeeRepository.findEmployeeByDeletedFalse();
                case EXPENSE_ONLY -> expenseRepository.findByExpenseDateBetweenAndDeletedFalse(start, end);
                case INVOICE_MONTHLY -> invoiceRepository.findByCreatedAtBetweenAndDeletedFalse(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
            };

            return pdfExportService.generatePdf(reportData, request.getType());
        }

    public List<?> getReportData(ReportFilterRequest request) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }

        return switch (request.getType()) {
            case IMPORT_ONLY -> importRepository.findByImportDateBetweenAndDeletedFalse(start, end);
            case EXPORT_ONLY -> exportRepository.findAllByDeletedFalseAndExportDateBetween(start, end);
            case IMPORT_EXPORT -> {
                Map<String, Object> map = new HashMap<>();
                map.put("imports", importRepository.findByImportDateBetweenAndDeletedFalse(start, end));
                map.put("exports", exportRepository.findAllByDeletedFalseAndExportDateBetween(start, end));
                yield List.of(map);
            }
            case REVENUE_SUMMARY -> {
                RevenueFilterRequest revenueRequest = new RevenueFilterRequest(start, end);
                RevenueResponse revenue = revenueService.getRevenueSummary(revenueRequest);
                yield List.of(revenue);
            }
            case EMPLOYEE_SALARY -> employeeRepository.findEmployeeByDeletedFalse();
            case EXPENSE_ONLY -> expenseRepository.findByExpenseDateBetweenAndDeletedFalse(start, end);
            case INVOICE_MONTHLY -> invoiceRepository.findByCreatedAtBetweenAndDeletedFalse(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
        };
    }

}
