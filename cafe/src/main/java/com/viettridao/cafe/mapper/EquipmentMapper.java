package com.viettridao.cafe.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.EquipmentEntity;

@Component
public class EquipmentMapper extends BaseMapper<EquipmentEntity, CreateEquipmentRequest, EquipmentResponse> {

	public EquipmentMapper(ModelMapper modelMapper) {
		super(modelMapper, EquipmentEntity.class, CreateEquipmentRequest.class, EquipmentResponse.class);
	}

	@Override
	public EquipmentResponse toDto(EquipmentEntity entity) {
		return super.toDto(entity);
	}

	@Override
	public List<EquipmentResponse> toDtoList(List<EquipmentEntity> entities) {
		return entities.stream().map(this::toDto).toList();
	}

	public void updateEntityFromUpdateRequest(UpdateEquipmentRequest dto, EquipmentEntity entity) {
		modelMapper.map(dto, entity);
	}

	public BudgetViewResponse toBudgetDto(EquipmentEntity entity) {
		BudgetViewResponse dto = new BudgetViewResponse();
		dto.setDate(entity.getPurchaseDate());
		dto.setIncome(0.0); // vì là chi, không phải thu
		dto.setExpense(entity.getPurchasePrice());
		return dto;
	}
}
