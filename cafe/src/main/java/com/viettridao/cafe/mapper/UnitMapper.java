package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.unit.UnitResponse;
import com.viettridao.cafe.model.UnitEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UnitMapper {
    private final ModelMapper modelMapper;

    public UnitResponse toUnitResponse(UnitEntity entity){
        return modelMapper.map(entity, UnitResponse.class);
    }

    public List<UnitResponse> toUnitResponseList(List<UnitEntity> entities){
        return entities.stream().map(this::toUnitResponse).toList();
    }

}
