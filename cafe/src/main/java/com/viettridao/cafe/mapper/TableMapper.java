package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.tables.TableResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.TableEntity;

@Component
public class TableMapper extends BaseMapper<TableEntity, Object, TableResponse> {
	public TableMapper(ModelMapper modelMapper) {
		super(modelMapper, TableEntity.class, Object.class, TableResponse.class);
	}
}
