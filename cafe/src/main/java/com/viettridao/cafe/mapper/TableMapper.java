package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.model.TableEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TableMapper {
    private final ModelMapper modelMapper;

    public TableResponse toResponse(TableEntity entity){
        return modelMapper.map(entity, TableResponse.class);
    }

    public List<TableResponse> toListResponse(List<TableEntity> entities){
        return entities.stream().map(this::toResponse).toList();
    }
}
