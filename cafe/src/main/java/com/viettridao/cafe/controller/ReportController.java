
package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.report.ReportFilterRequest;
import com.viettridao.cafe.dto.response.report.ReportItemResponse;
import com.viettridao.cafe.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    // Xuất file báo cáo
    @GetMapping("/export")
    public void exportReport(
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "fileFormat", required = false, defaultValue = "txt") String fileFormat,
            HttpServletResponse response) throws IOException {
        LocalDate from = null;
        LocalDate to = null;
        try {
            if (fromDate != null && !fromDate.isBlank())
                from = LocalDate.parse(fromDate);
            if (toDate != null && !toDate.isBlank())
                to = LocalDate.parse(toDate);
        } catch (Exception e) {
            // fallback: ignore parse error, let service handle null
        }
        List<ReportItemResponse> reportList = reportService.getReport(from, to, category);
        String filename = "report-" + LocalDate.now() + "." + fileFormat;
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        response.setContentType("text/plain");
        if ("xls".equals(fileFormat)) {
            response.setContentType("application/vnd.ms-excel");
        } else if ("sql".equals(fileFormat)) {
            response.setContentType("application/sql");
        }
        PrintWriter writer = response.getWriter();
        writer.println("Ngay,Thu,Chi");
        for (ReportItemResponse item : reportList) {
            writer.println(item.getNgay() + "," + item.getThu() + "," + item.getChi());
        }
        writer.flush();
        writer.close();
    }

    // In file báo cáo (giả lập, trả về file PDF mẫu)
    @GetMapping("/print")
    public void printReport(
            @RequestParam(value = "category", required = false, defaultValue = "all") String category,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate,
            @RequestParam(value = "paperSize", required = false, defaultValue = "A4") String paperSize,
            @RequestParam(value = "printer", required = false, defaultValue = "Hp Laser") String printer,
            @RequestParam(value = "copies", required = false, defaultValue = "1") int copies,
            HttpServletResponse response) throws IOException {
        // Có thể lấy dữ liệu báo cáo nếu cần, parse ngày như trên
        response.setHeader("Content-Disposition", "attachment; filename=report-print.pdf");
        response.setContentType("application/pdf");
        response.getOutputStream().write("PDF PRINT DEMO".getBytes());
        response.getOutputStream().flush();
    }

    @GetMapping
    public String getReport(@ModelAttribute ReportFilterRequest filter,
            BindingResult bindingResult,
            Model model) {
        // Validate ngày: fromDate <= toDate chỉ ở controller
        LocalDate from = null;
        LocalDate to = null;
        if (!bindingResult.hasErrors()) {
            try {
                if (filter.getFromDate() != null && !filter.getFromDate().isBlank())
                    from = LocalDate.parse(filter.getFromDate());
                if (filter.getToDate() != null && !filter.getToDate().isBlank())
                    to = LocalDate.parse(filter.getToDate());
                if (from != null && to != null && from.isAfter(to)) {
                    bindingResult.rejectValue("fromDate", "fromDate.afterToDate",
                            "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!");
                }
            } catch (Exception e) {
                bindingResult.reject("date.parse", "Định dạng ngày không hợp lệ!");
            }
        }
        List<ReportItemResponse> reportList = List.of();
        long tongThu = 0;
        long tongChi = 0;
        // Không validate ở service, chỉ lấy dữ liệu nếu không có lỗi
        if (!bindingResult.hasErrors()) {
            reportList = reportService.getReport(from, to, filter.getCategory());
            tongThu = reportList.stream().mapToLong(ReportItemResponse::getThu).sum();
            tongChi = reportList.stream().mapToLong(ReportItemResponse::getChi).sum();
        }
        model.addAttribute("reportList", reportList);
        model.addAttribute("tongThu", tongThu);
        model.addAttribute("tongChi", tongChi);
        model.addAttribute("category", filter.getCategory());
        model.addAttribute("fromDate", filter.getFromDate());
        model.addAttribute("toDate", filter.getToDate());
        model.addAttribute("org.springframework.validation.BindingResult.filter", bindingResult);
        model.addAttribute("filter", filter);
        return "reports/report";
    }
}
