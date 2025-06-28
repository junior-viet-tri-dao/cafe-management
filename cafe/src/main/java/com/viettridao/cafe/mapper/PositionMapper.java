package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.position.PositionResponse;
import com.viettridao.cafe.model.PositionEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PositionMapper {
    private final ModelMapper modelMapper;

    public PositionResponse toPositionResponse(PositionEntity entity) {
        return modelMapper.map(entity, PositionResponse.class);
    }

    public List<PositionResponse> toListPositionResponse(List<PositionEntity> entities) {
        return entities.stream().map(this::toPositionResponse).toList();
    }
}
