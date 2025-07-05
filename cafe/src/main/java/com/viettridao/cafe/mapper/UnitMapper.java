package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.CreateUnitRequest;
import com.viettridao.cafe.dto.response.unit.UnitResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.UnitEntity;

@Component
public class UnitMapper extends BaseMapper<UnitEntity, CreateUnitRequest, UnitResponse> {

	public UnitMapper(ModelMapper modelMapper) {
		super(modelMapper, UnitEntity.class, CreateUnitRequest.class, UnitResponse.class);
	}

}
