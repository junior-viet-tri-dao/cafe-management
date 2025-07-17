package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.model.TableEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TableMapper {

    private final ModelMapper modelMapper;
    
    public TableResponse convertToDto(TableEntity table) {
        return modelMapper.map(table, TableResponse.class);
    }
}
