package com.viettridao.cafe.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.ArrayList;

import com.viettridao.cafe.dto.request.revenue.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;
import com.viettridao.cafe.service.revenue.IRevenueService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/revenue")
public class RevenueController {

    private final IRevenueService revenueService;

    @GetMapping
    public String viewRevenue(
            @Valid
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,

            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,

            Model model
    ) {
        // Nếu chưa nhập thì mặc định lấy đầu năm 2025
        if (startDate == null) {
            startDate = LocalDate.of(2025, 1, 1);
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
        return "revenue/list";
    }

}