package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.model.PromotionEntity;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMapper {
    private final ModelMapper modelMapper;

    public PromotionResponse convertToDto(PromotionEntity promotion) {
        return modelMapper.map(promotion, PromotionResponse.class);
    }

}
