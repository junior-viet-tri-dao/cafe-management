package com.viettridao.cafe.controller;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.response.report.ItemReportResponse;
import com.viettridao.cafe.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/report")
    public String report(

            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "type", required = false, defaultValue = "ALL") ReportType type, Model model) {
        try {
            if (from != null)
            {
                List<ItemReportResponse> reports = reportService.getReport(from, to, type);

                model.addAttribute("reports", reports);
                model.addAttribute("from", from);
                model.addAttribute("to", to);
                model.addAttribute("type", type);
                model.addAttribute("types", Arrays.asList(ReportType.values()));
            }
        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi tải dữ liệu thống kê.");
        }


        return "report";
    }

}