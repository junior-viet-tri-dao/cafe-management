package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.viettridao.cafe.dto.request.equipment.EquipmentCreateRequest;
import com.viettridao.cafe.dto.request.equipment.EquipmentUpdateRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.model.EquipmentEntity;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    @Mapping(target = "deleted", constant = "false")
    EquipmentEntity toEntity(EquipmentCreateRequest request);

    EquipmentResponse toResponse(EquipmentEntity entity);

    EquipmentUpdateRequest toUpdateRequest(EquipmentEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromRequest(EquipmentUpdateRequest request, @MappingTarget EquipmentEntity entity);

}
