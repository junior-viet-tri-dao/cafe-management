package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.expense.ExpenseRequest;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;
import com.viettridao.cafe.model.ExpenseEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExpenseMapper {

    private final ModelMapper modelMapper;

    public ExpenseResponse convertToDto(ExpenseEntity expense) {
        return modelMapper.map(expense, ExpenseResponse.class);
    }

    public ExpenseEntity convertToEntity(ExpenseRequest req) {
        return modelMapper.map(req, ExpenseEntity.class);
    }
}
