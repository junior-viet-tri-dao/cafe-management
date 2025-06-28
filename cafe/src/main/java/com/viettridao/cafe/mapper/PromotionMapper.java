package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.model.PromotionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PromotionMapper {
    private final ModelMapper modelMapper;

    public PromotionResponse toPromotionResponse(PromotionEntity entity){
        return modelMapper.map(entity, PromotionResponse.class);
    }

    public List<PromotionResponse> toPromotionResponseList(List<PromotionEntity> entities){
        return entities.stream().map(this::toPromotionResponse).toList();
    }
}
