package com.viettridao.cafe.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.viettridao.cafe.dto.request.expense.ExpenseRequest;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;
import com.viettridao.cafe.dto.response.report.ReportResponse;
import com.viettridao.cafe.service.ExpenseService;
import com.viettridao.cafe.service.ReportService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ExpenseController extends BaseController {

    private final ReportService reportService;
    private final ExpenseService expenseService;

    @GetMapping("/expense")
    public String showExpense(Model model) {
        model.addAttribute("totalRevenue", 0L);
        model.addAttribute("totalExpense", 0L);
        return "expenses/expense";
    }

    @GetMapping("/expense/by-date-range")
    public String getReportByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            Model model) {
        List<ReportResponse> reportList = reportService.getReportByDateRange(LocalDate.parse(startDate),
                LocalDate.parse(endDate));

        for (ReportResponse report : reportList) {
            System.out.println("-----------------------");
            System.out.println("Date: " + report.getDateReport());
            System.out.println("Total Revenue: " + report.getRevenue());
            System.out.println("Total Expense: " + report.getExpense());
        }
        model.addAttribute("reportList", reportList);

        return "expenses/expense";
    }

    @GetMapping("/expense/create")
    public String showCreateExpense(Model model) {

        List<ExpenseResponse> expenseList = expenseService.getAllExpenses();
        model.addAttribute("expenseList", expenseList);
        return "expenses/expense-create";
    }

    @PostMapping("/expense/create")
    public String createExpense(@ModelAttribute ExpenseRequest request, Model model, HttpSession session) {
        try {
            // Lưu expense mới
            expenseService.createExpense(request, getCurrentUser(session).getId());

            // Lấy lại danh sách expense mới nhất
            List<ExpenseResponse> expenseList = expenseService.getAllExpenses();
            model.addAttribute("expenseList", expenseList);

            // Thêm thông báo thành công
            model.addAttribute("successMessage", "Thêm chi tiêu thành công!");
        } catch (Exception ex) {
            // Lấy lại danh sách expense để hiển thị
            List<ExpenseResponse> expenseList = expenseService.getAllExpenses();
            model.addAttribute("expenseList", expenseList);

            // Thêm thông báo lỗi
            model.addAttribute("errorMessage", "Thêm chi tiêu thất bại: " + ex.getMessage());
        }
        return "expenses/expense-create";
    }
}
