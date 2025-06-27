package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.model.EquipmentEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EquipmentMapper {
    private final ModelMapper modelMapper;

    public EquipmentResponse convertToDto(EquipmentEntity equipment) {
        return modelMapper.map(equipment, EquipmentResponse.class);
    }

    public EquipmentEntity convertToEntity(CreateEquipmentRequest request) {
        return modelMapper.map(request, EquipmentEntity.class);
    }
}
