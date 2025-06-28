package com.viettridao.cafe.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.PromotionEntity;

@Component
public class PromotionMapper extends BaseMapper<PromotionEntity, CreatePromotionRequest, PromotionResponse> {

	public PromotionMapper(ModelMapper modelMapper) {
		super(modelMapper, PromotionEntity.class, CreatePromotionRequest.class, PromotionResponse.class);
	}

	@Override
	public PromotionResponse toDto(PromotionEntity entity) {
		PromotionResponse res = super.toDto(entity);

		return res;
	}

	@Override
	public List<PromotionResponse> toDtoList(List<PromotionEntity> entities) {
		return entities.stream().map(this::toDto).toList();
	}

}
