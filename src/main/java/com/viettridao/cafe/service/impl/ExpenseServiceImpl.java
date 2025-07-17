package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.expense.ExpenseRequest;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;
import com.viettridao.cafe.mapper.ExpenseMapper;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.service.ExpenseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseMapper expenseMapper;
    private final ExpenseRepository expenseRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<ExpenseResponse> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(expenseMapper::convertToDto)
                .toList();
    }

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request, Integer accountId) {
        try {
            ExpenseEntity expenseEntity = expenseMapper.convertToEntity(request);
            expenseEntity.setAccount(accountRepository.findById(accountId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản")));
            expenseEntity = save(expenseEntity);
            return expenseMapper.convertToDto(expenseEntity);
        } catch (Exception ex) {
            // Ghi log nếu cần
            throw new RuntimeException("Lỗi khi tạo chi tiêu: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ExpenseEntity save(ExpenseEntity expenseEntity) {
        return expenseRepository.save(expenseEntity);
    }

    @Override
    public List<ExpenseEntity> getAllByExpenseDateBetween(LocalDate start, LocalDate end) {
        return expenseRepository.findAllByExpenseDateBetween(start, end);
    }

    // Implement methods for managing expenses here
    // For example, methods to create, update, delete, and retrieve expenses

}
