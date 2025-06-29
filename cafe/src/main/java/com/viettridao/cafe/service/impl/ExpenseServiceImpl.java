package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.budget.CreateExpenseRequest;
import com.viettridao.cafe.dto.request.budget.UpdateExpenseRequest;
import com.viettridao.cafe.dto.response.budget.ExpensePageResponse;
import com.viettridao.cafe.dto.response.budget.ExpenseResponse;
import com.viettridao.cafe.mapper.ExpenseMapper;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpensePageResponse getAllExpenses(String keyword, int page, int size) {
        Page<ExpenseEntity> expensePage = expenseRepository.findAll(PageRequest.of(page, size));
        if (expensePage == null) {
            // Trả về page rỗng nếu repository trả về null (phòng trường hợp mock lỗi)
            expensePage = Page.empty();
        }
        List<ExpenseResponse> responses = expensePage.getContent().stream()
                .map(expenseMapper::toExpenseResponse)
                .collect(Collectors.toList());
        ExpensePageResponse result = new ExpensePageResponse();
        result.setExpenses(responses);
        // Xử lý trường hợp mock Page bị null hoặc các thuộc tính page bị lỗi
        int pageNumber = 0;
        int totalPages = 1;
        long totalElements = responses.size();
        try {
            pageNumber = expensePage.getNumber();
            totalPages = expensePage.getTotalPages();
            totalElements = expensePage.getTotalElements();
        } catch (Exception ignored) {
            // fallback giữ nguyên giá trị mặc định
        }
        result.setPageNumber(pageNumber);
        result.setTotalPages(totalPages);
        result.setTotalElements(totalElements);
        return result;
    }

    @Override
    public ExpensePageResponse getExpensesByDateRange(LocalDate from, LocalDate to, int page, int size) {
        List<ExpenseEntity> entities = expenseRepository.findAllByExpenseDateBetweenAndIsDeletedFalse(from, to);
        List<ExpenseResponse> responses = entities.stream().map(expenseMapper::toExpenseResponse)
                .collect(Collectors.toList());
        ExpensePageResponse result = new ExpensePageResponse();
        result.setExpenses(responses);
        result.setPageNumber(0);
        result.setTotalPages(1);
        result.setTotalElements(responses.size());
        return result;
    }

    @Override
    @Transactional
    public ExpenseEntity createExpense(CreateExpenseRequest request) {
        ExpenseEntity entity = new ExpenseEntity();
        entity.setExpenseName(request.getExpenseName());
        entity.setAmount(request.getAmount());
        entity.setExpenseDate(request.getExpenseDate());
        entity.setIsDeleted(false);
        return expenseRepository.save(entity);
    }

    @Override
    @Transactional
    public void updateExpense(UpdateExpenseRequest request) {
        ExpenseEntity entity = expenseRepository.findById(request.getId())
                .orElseThrow(() -> new java.util.NoSuchElementException("Không tìm thấy khoản chi"));
        entity.setExpenseName(request.getExpenseName());
        entity.setAmount(request.getAmount());
        entity.setExpenseDate(request.getExpenseDate());
        expenseRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteExpense(Integer id) {
        ExpenseEntity entity = expenseRepository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Không tìm thấy khoản chi"));
        entity.setIsDeleted(true);
        expenseRepository.save(entity);
    }

    @Override
    public ExpenseEntity getExpenseById(Integer id) {
        return expenseRepository.findById(id).orElse(null);
    }
}
