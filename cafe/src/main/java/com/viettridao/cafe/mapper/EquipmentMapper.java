package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.dto.response.expense.BudgetResponse;
import com.viettridao.cafe.model.EquipmentEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EquipmentMapper {
    private final ModelMapper modelMapper;

    // 1. mapper by manual
    public EquipmentResponse toResponse(EquipmentEntity entity) {
        EquipmentResponse equipmentResponse = new EquipmentResponse();
        equipmentResponse.setId(entity.getId());
        equipmentResponse.setEquipmentName(entity.getEquipmentName());
        equipmentResponse.setNotes(entity.getNotes());
        equipmentResponse.setPurchaseDate(entity.getPurchaseDate());
        equipmentResponse.setPurchasePrice(entity.getPurchasePrice());
        equipmentResponse.setIsDeleted(entity.getIsDeleted());
        equipmentResponse.setQuantity(entity.getQuantity());

        return equipmentResponse;
    }

    // 2. mapper by library
    public EquipmentResponse toEquipmentResponse(EquipmentEntity entity) {
        EquipmentResponse equipmentResponse = new EquipmentResponse();
        modelMapper.map(entity, equipmentResponse);

        return equipmentResponse;
    }

    // 1. Using toResponse List at manual mapper
    public List<EquipmentResponse> toResponseList(List<EquipmentEntity> entities){
        return entities.stream().map(this::toEquipmentResponse).toList();
    }

    // 2. Using toEquipmentResponseList List at manual library
    public List<EquipmentResponse> toEquipmentResponseList(List<EquipmentEntity> entities) {
        return entities.stream().map(this::toEquipmentResponse).toList();
    }


    // New mapper: EquipmentEntity → BudgetViewResponse
    public BudgetResponse toBudgetDto(EquipmentEntity entity) {
        if (entity == null) {
            return null;
        }

        BudgetResponse dto = new BudgetResponse();
        dto.setDate(entity.getPurchaseDate());
        dto.setExpense(entity.getPurchasePrice());
        dto.setIncome(0.0);
        return dto;
    }
}
