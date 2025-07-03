package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;
import com.viettridao.cafe.model.ExpenseEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExpenseMapper {
    private final ModelMapper modelMapper;

    public ExpenseResponse toResponse(ExpenseEntity entity) {
        ExpenseResponse response = new ExpenseResponse();
        modelMapper.map(entity, response);
        if(entity.getAccount() != null) {
            response.setAccount(modelMapper.map(entity.getAccount(), AccountResponse.class));
        }
        return response;
    }

    public List<ExpenseResponse> toResponse(List<ExpenseEntity> entities) {
        return entities.stream().map(this::toResponse).toList();
    }

}
