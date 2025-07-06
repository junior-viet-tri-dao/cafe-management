package com.viettridao.cafe.controller;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.request.report.ReportFilterRequest;
import com.viettridao.cafe.service.report.IReportService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final IReportService reportService;

    @GetMapping
    public String showReportForm() {
        return "report/report-management";
    }

    @GetMapping("/download")
    public void downloadPdf(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("type") String type,
            HttpServletResponse response
    ) throws IOException {
        ReportFilterRequest request = new ReportFilterRequest();
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        try {
            request.setType(ReportType.valueOf(type));
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Loại báo cáo không hợp lệ");
            return;
        }


        byte[] pdfData = reportService.generateReport(request);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report.pdf");
        response.getOutputStream().write(pdfData);
        response.getOutputStream().flush();
    }

}
