package com.viettridao.cafe.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.EmployeeEntity;

@Component
public class EmployeeMapper extends BaseMapper<EmployeeEntity, CreateEmployeeRequest, EmployeeResponse> {

	public EmployeeMapper(ModelMapper modelMapper) {
		super(modelMapper, EmployeeEntity.class, CreateEmployeeRequest.class, EmployeeResponse.class);
	}

	@Override
	public EmployeeResponse toDto(EmployeeEntity entity) {
		EmployeeResponse response = super.toDto(entity);

		var position = entity.getPosition();
		if (position != null) {
			response.setPositionId(position.getId());
			response.setPositionName(position.getPositionName());
			response.setSalary(position.getSalary());
		}

		var account = entity.getAccount();
		if (account != null) {
			response.setUsername(account.getUsername());
			response.setPassword(account.getPassword());
			response.setImageUrl(account.getImageUrl());
		}

		return response;
	}

	@Override
	public List<EmployeeResponse> toDtoList(List<EmployeeEntity> entities) {
		return entities.stream().map(this::toDto).toList();
	}

	public void updateEntityFromUpdateRequest(UpdateEmployeeRequest dto, EmployeeEntity entity) {
		modelMapper.map(dto, entity);
	}
}
