package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.viettridao.cafe.dto.request.PositionRequest;
import com.viettridao.cafe.dto.response.position.PositionResponse;
import com.viettridao.cafe.model.PositionEntity;

@Mapper(componentModel = "spring")
public interface PositionMapper {

	PositionEntity fromRequest(PositionRequest request);

	PositionResponse toDto(PositionEntity entity);

	List<PositionResponse> toDtoList(List<PositionEntity> entities);
}
