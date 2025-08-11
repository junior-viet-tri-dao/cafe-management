package com.viettridao.cafe.service.impl;


import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.request.reservation.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueItemResponse;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.*;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class RevenueServiceImpl implements RevenueService {
    private final InvoiceRepository invoiceRepository;
    private final ImportRepository importRepository;
    private final EquipmentRepository equipmentRepository;
    private final ExpenseRepository expenseRepository;


    @Override
    public RevenueResponse getRevenueSummary(RevenueFilterRequest request) {
        LocalDate start = request.getStartDate();
        LocalDate end = request.getEndDate();

        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Ngày không hợp lệ");
        }

        Map<LocalDate, RevenueItemResponse> dailyMap = new TreeMap<>();

        for (InvoiceEntity invoice : invoiceRepository.findByCreatedAtBetweenAndIsDeletedFalse(start.atStartOfDay(), end.plusDays(1).atStartOfDay())) {
            if (invoice.getStatus() != InvoiceStatus.PAID) {
                continue;
            }
            LocalDate date = invoice.getCreatedAt().toLocalDate();
            dailyMap.putIfAbsent(date, new RevenueItemResponse());
            RevenueItemResponse item = dailyMap.get(date);
            item.setDate(date);
            item.setIncome(Optional.ofNullable(item.getIncome()).orElse(0.0) + invoice.getTotalAmount());
        }

        for (ImportEntity imp : importRepository.findByImportDateBetweenAndIsDeletedFalse(start, end)) {
            LocalDate date = imp.getImportDate();
            dailyMap.putIfAbsent(date, new RevenueItemResponse());
            RevenueItemResponse item = dailyMap.get(date);
            item.setDate(date);
            item.setExpense(Optional.ofNullable(item.getExpense()).orElse(0.0) + imp.getTotalAmount());
        }

        for (EquipmentEntity equip : equipmentRepository.findByPurchaseDateBetweenAndIsDeletedFalse(start, end)) {
            LocalDate date = equip.getPurchaseDate();
            dailyMap.putIfAbsent(date, new RevenueItemResponse());
            RevenueItemResponse item = dailyMap.get(date);
            item.setDate(date);
            item.setExpense(Optional.ofNullable(item.getExpense()).orElse(0.0) + equip.getPurchasePrice());
        }

        for (ExpenseEntity expense : expenseRepository.findByExpenseDateBetweenAndIsDeletedFalse(start, end)) {
            LocalDate date = expense.getExpenseDate();
            dailyMap.putIfAbsent(date, new RevenueItemResponse());
            RevenueItemResponse item = dailyMap.get(date);
            item.setDate(date);
            item.setExpense(Optional.ofNullable(item.getExpense()).orElse(0.0) + expense.getAmount());
        }

        double totalIncome = dailyMap.values().stream().mapToDouble(i -> Optional.ofNullable(i.getIncome()).orElse(0.0)).sum();
        double totalExpense = dailyMap.values().stream().mapToDouble(i -> Optional.ofNullable(i.getExpense()).orElse(0.0)).sum();

        RevenueResponse response = new RevenueResponse();
        response.setSummaries(new ArrayList<>(dailyMap.values()));
        response.setTotalIncome(totalIncome);
        response.setTotalExpense(totalExpense);
        return response;
    }
}