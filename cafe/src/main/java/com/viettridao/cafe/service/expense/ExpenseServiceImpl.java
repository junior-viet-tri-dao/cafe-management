package com.viettridao.cafe.service.expense;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

import com.viettridao.cafe.dto.request.expense.ExpenseCreateRequest;
import com.viettridao.cafe.dto.request.expense.ExpenseUpdateRequest;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;
import com.viettridao.cafe.mapper.ExpenseMapper;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.repository.ExpenseRepository;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements IExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseResponse> getExpenseAll() {
        return expenseRepository.getAllByDeletedFalse()
                .stream()
                .map(expenseMapper::toResponse)
                .toList();
    }

    @Override
    public void createExpense(ExpenseCreateRequest request) {
        ExpenseEntity expenseEntity = expenseMapper.toEntity(request);
        expenseRepository.save(expenseEntity);
    }

    @Override
    public ExpenseUpdateRequest getUpdateForm(Integer id) {
        return expenseMapper.toUpdateRequest(findExpenseOrThrow(id));
    }

    @Override
    public void updateExpense(Integer id, ExpenseUpdateRequest request) {
        ExpenseEntity existing = findExpenseOrThrow(id);
        expenseMapper.updateEntityFromRequest(request, existing);
        expenseRepository.save(existing);
    }

    @Override
    public void deleteExpense(Integer id) {
        ExpenseEntity entity = findExpenseOrThrow(id);
        entity.setDeleted(true);
        expenseRepository.save(entity);
    }

    private ExpenseEntity findExpenseOrThrow(Integer id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
    }
}
