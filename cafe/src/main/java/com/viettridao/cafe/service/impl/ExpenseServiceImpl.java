package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.expense.ExpenseCreateRequest;
import com.viettridao.cafe.dto.request.expense.ExpenseUpdateRequest;
import com.viettridao.cafe.dto.response.expense.ExpensePageResponse;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;
import com.viettridao.cafe.mapper.ExpenseMapper;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final AccountRepository accountRepository;


    @Override
    public List<ExpenseResponse> getExpenseAll() {
        return expenseRepository.getAllByIsDeletedFalse()
                .stream()
                .map(expenseMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public void createExpense(ExpenseCreateRequest request) {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setExpenseDate(request.getExpenseDate());
        expenseEntity.setExpenseName(request.getExpenseName());
        expenseEntity.setAmount(request.getAmount());
        expenseEntity.setIsDeleted(false);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AccountEntity account = accountRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản: " + username));
        expenseEntity.setAccount(account);

        expenseRepository.save(expenseEntity);
    }

    @Transactional
    @Override
    public void updateExpense(ExpenseUpdateRequest request) {
       ExpenseEntity entity = getExpenseById(request.getId());
       entity.setExpenseName(request.getExpenseName());
       entity.setAmount(request.getAmount());
       entity.setExpenseDate(request.getExpenseDate());

       expenseRepository.save(entity);
    }

    @Transactional
    @Override
    public void deleteExpense(Integer id) {
        ExpenseEntity entity = getExpenseById(id);
        entity.setIsDeleted(true);
        expenseRepository.save(entity);
    }

    @Override
    public ExpenseEntity getExpenseById(Integer id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiêu có id=" + id));
    }

    @Override
    public ExpensePageResponse getAllExpense(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ExpenseEntity> expenseEntities = expenseRepository.findAllByIsDeletedFalse(pageable);

        ExpensePageResponse expensePageResponse = new ExpensePageResponse();
        expensePageResponse.setPageNumber(expenseEntities.getNumber());
        expensePageResponse.setTotalElements(expenseEntities.getTotalElements());
        expensePageResponse.setTotalPages(expenseEntities.getTotalPages());
        expensePageResponse.setPageSize(expenseEntities.getSize());
        expensePageResponse.setExpenses(expenseMapper.toResponse(expenseEntities.getContent()));

        return expensePageResponse;
    }
}
