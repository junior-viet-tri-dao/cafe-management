package com.viettridao.cafe.controller;


import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.request.reservation.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/revenue")
public class RevenueController {
    private final RevenueService revenueService;

    @GetMapping
    public String viewRevenue(
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            Model model
    ) {
        // Nếu chưa nhập thì lấy toàn bộ dữ liệu
        if (startDate == null) {
            startDate = LocalDate.of(2000, 1, 1); // hoặc ngày sớm nhất bạn muốn hiển thị
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        RevenueFilterRequest filter = new RevenueFilterRequest();
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);

        RevenueResponse revenue = new RevenueResponse();
        revenue.setSummaries(new ArrayList<>());
        revenue.setTotalIncome(0.0);
        revenue.setTotalExpense(0.0);

        try {
            revenue = revenueService.getRevenueSummary(filter);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("filter", filter);
        model.addAttribute("revenue", revenue);
        return "/revenues/revenue";
    }

}
