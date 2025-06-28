package com.viettridao.cafe.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.PositionRequest;
import com.viettridao.cafe.dto.response.position.PositionResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.PositionEntity;

@Component
public class PositionMapper extends BaseMapper<PositionEntity, PositionRequest, PositionResponse> {

	public PositionMapper(ModelMapper modelMapper) {
		super(modelMapper, PositionEntity.class, PositionRequest.class, PositionResponse.class);
	}

	@Override
	public PositionResponse toDto(PositionEntity entity) {
		return super.toDto(entity);
	}

	@Override
	public List<PositionResponse> toDtoList(List<PositionEntity> entities) {
		return entities.stream().map(this::toDto).toList();
	}
}
