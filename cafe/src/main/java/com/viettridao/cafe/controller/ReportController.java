package com.viettridao.cafe.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;
import com.viettridao.cafe.service.ReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/statistics")
    public String viewReport(
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(name = "type", required = false, defaultValue = "ALL") ReportType type,
            Model model) {

        try {
            if (from == null) from = LocalDate.now().minusDays(7);
            if (to == null) to = LocalDate.now();

            List<ReportItemResponse> reports = reportService.getReport(from, to, type);

            double totalRevenue = reports.stream()
                    .mapToDouble(r -> r.getRevenue() != null ? r.getRevenue() : 0.0)
                    .sum();
            double totalExpense = reports.stream()
                    .mapToDouble(r -> r.getExpense() != null ? r.getExpense() : 0.0)
                    .sum();

            model.addAttribute("reports", reports);
            model.addAttribute("from", from);
            model.addAttribute("to", to);
            model.addAttribute("type", type);
            model.addAttribute("types", Arrays.asList(ReportType.values()));
            model.addAttribute("totalRevenue", totalRevenue);
            model.addAttribute("totalExpense", totalExpense);

        } catch (Exception e) {
            model.addAttribute("error", "Đã xảy ra lỗi khi tải dữ liệu thống kê.");
        }

        return "report/statistics";
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam("type") ReportType type,
            @RequestParam("format") String format) {

        try {
            List<ReportItemResponse> reports = reportService.getReport(from, to, type);

            StringBuilder content = new StringBuilder("Ngày\tThu\tChi\n");
            for (ReportItemResponse r : reports) {
                content.append(r.getDate()).append("\t")
                       .append(r.getRevenue() != null ? r.getRevenue() : 0).append("\t")
                       .append(r.getExpense() != null ? r.getExpense() : 0).append("\n");
            }

            byte[] data = content.toString().getBytes(StandardCharsets.UTF_8);
            String fileName = "report." + format.toLowerCase();
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentLength(data.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            byte[] errorData = "Lỗi khi xuất báo cáo.".getBytes(StandardCharsets.UTF_8);
            ByteArrayResource resource = new ByteArrayResource(errorData);

            return ResponseEntity.internalServerError()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=error.txt")
                    .contentLength(errorData.length)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);
        }
    }

    @PostMapping("/print")
    public String printReport(
            @RequestParam("paperSize") String paperSize,
            @RequestParam("printerName") String printerName,
            @RequestParam("copies") int copies,
            RedirectAttributes redirectAttributes) {

        try {
            String message = String.format("Đã gửi lệnh in: %d bản, khổ giấy %s, máy in %s",
                    copies, paperSize, printerName);
            redirectAttributes.addFlashAttribute("success", message);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi in báo cáo.");
        }

        return "redirect:/report/statistics";
    }
}
