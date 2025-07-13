package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.model.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mappings({ @Mapping(source = "position.id", target = "positionId"),
			@Mapping(source = "position.positionName", target = "positionName"),
			@Mapping(source = "position.salary", target = "salary"),
			@Mapping(source = "account.username", target = "username"),
			@Mapping(source = "account.password", target = "password"),
			@Mapping(source = "account.imageUrl", target = "imageUrl") })
	EmployeeResponse toDto(EmployeeEntity entity);

	List<EmployeeResponse> toDtoList(List<EmployeeEntity> entities);

	EmployeeEntity fromCreateRequest(CreateEmployeeRequest dto);

	void updateEntityFromUpdateRequest(UpdateEmployeeRequest dto, @MappingTarget EmployeeEntity entity);
}
