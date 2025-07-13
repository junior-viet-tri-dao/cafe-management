package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.CreateUnitRequest;
import com.viettridao.cafe.dto.response.unit.UnitResponse;
import com.viettridao.cafe.model.UnitEntity;

@Mapper(componentModel = "spring")
public interface UnitMapper {

	@Mapping(target = "id", ignore = true) 
	UnitEntity fromRequest(CreateUnitRequest request);

	UnitResponse toDto(UnitEntity entity);

	List<UnitResponse> toDtoList(List<UnitEntity> entities);
}
