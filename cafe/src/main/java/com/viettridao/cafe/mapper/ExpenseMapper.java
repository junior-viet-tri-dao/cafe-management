package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.viettridao.cafe.dto.request.expense.ExpenseCreateRequest;
import com.viettridao.cafe.dto.request.expense.ExpenseUpdateRequest;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;
import com.viettridao.cafe.model.ExpenseEntity;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "account", ignore = true)
    ExpenseEntity toEntity(ExpenseCreateRequest request);

    ExpenseResponse toResponse(ExpenseEntity entity);

    ExpenseUpdateRequest toUpdateRequest(ExpenseEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromRequest(ExpenseUpdateRequest request, @MappingTarget ExpenseEntity entity);

}
