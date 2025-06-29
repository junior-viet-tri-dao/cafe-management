package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.unit.UnitRequest;
import com.viettridao.cafe.dto.response.unit.UnitResponse;
import com.viettridao.cafe.model.UnitEntity;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    UnitEntity toEntity(UnitRequest request);

    UnitResponse toResponse(UnitEntity entity);
}
