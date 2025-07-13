package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.model.EquipmentEntity;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

	EquipmentResponse toDto(EquipmentEntity entity);

	List<EquipmentResponse> toDtoList(List<EquipmentEntity> entities);

	EquipmentEntity fromCreateRequest(CreateEquipmentRequest request);

	void updateEntityFromUpdateRequest(UpdateEquipmentRequest request, @MappingTarget EquipmentEntity entity);

	@Mapping(target = "date", source = "purchaseDate")
	@Mapping(target = "expense", source = "purchasePrice")
	@Mapping(target = "income", constant = "0.0")
	BudgetViewResponse toBudgetDto(EquipmentEntity entity);
}
