package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.model.PromotionEntity;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

	PromotionEntity fromRequest(CreatePromotionRequest request);

	PromotionResponse toDto(PromotionEntity entity);

	List<PromotionResponse> toDtoList(List<PromotionEntity> entities);
}
