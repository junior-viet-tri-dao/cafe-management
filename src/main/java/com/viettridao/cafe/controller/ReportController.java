package com.viettridao.cafe.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.viettridao.cafe.dto.response.report.ReportResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.ExpenseService;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.InvoiceService;
import com.viettridao.cafe.service.ReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ImportService importService;
    private final ExportService exportService;
    private final EmployeeService employeeService;
    private final ExpenseService expenseService;
    private final InvoiceService invoiceService;

    @GetMapping("/report")
    public String showReport(
            @RequestParam(value = "reportType", required = false, defaultValue = "all") String reportType,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            Model model) {

        if ("inventory".equals(reportType) && startDate != null && endDate != null) {
            // Lấy danh sách nhập hàng theo ngày
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<ImportEntity> importList = importService.getAllByImportDateRange(start, end);
            model.addAttribute("importList", importList);
            model.addAttribute("selectedType", "inventory");
        } else if ("export".equals(reportType) && startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<ExportEntity> exportList = exportService.getAllByExportDateRange(start, end);
            model.addAttribute("exportList", exportList);
            model.addAttribute("selectedType", "export");
        } else if ("import".equals(reportType) && startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<ImportEntity> importList = importService.getAllByImportDateRange(start, end);
            List<ExportEntity> exportList = exportService.getAllByExportDateRange(start, end);
            model.addAttribute("importList", importList);
            model.addAttribute("exportList", exportList);
            model.addAttribute("selectedType", "import");

        } else if ("employee".equals(reportType)) {
            List<EmployeeEntity> employeeList = employeeService.getAllEmployeeDescSalary();
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("selectedType", "employee");
        } else if ("expense".equals(reportType) && startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<ExpenseEntity> expenseList = expenseService.getAllByExpenseDateBetween(start, end);
            model.addAttribute("expenseList", expenseList);
            model.addAttribute("selectedType", "expense");
        } else if ("sale".equals(reportType) && startDate != null && endDate != null) {
            // Lấy danh sách hóa đơn bán hàng theo ngày
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            // Chuyển sang LocalDateTime để lấy cả ngày
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 59, 59);
            // Gọi service/repository để lấy danh sách hóa đơn
            List<InvoiceEntity> saleList = invoiceService
                    .getAllByInvoiceDateBetween(startDateTime, endDateTime);
            model.addAttribute("saleList", saleList);
            model.addAttribute("selectedType", "sale");
        }
        // ...xử lý các loại report khác nếu cần...

        return "reports/report";
    }

}
