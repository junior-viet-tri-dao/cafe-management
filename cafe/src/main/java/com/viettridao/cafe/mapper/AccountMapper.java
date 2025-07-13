package com.viettridao.cafe.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.model.AccountEntity;

@Mapper(componentModel = "spring")
public interface AccountMapper {

	@Mappings({ @Mapping(source = "employee.fullName", target = "fullName"),
			@Mapping(source = "employee.address", target = "address"),
			@Mapping(source = "employee.phoneNumber", target = "phoneNumber"),
			@Mapping(source = "employee.position.id", target = "positionId"),
			@Mapping(source = "employee.position.positionName", target = "positionName"),
			@Mapping(source = "employee.position.salary", target = "salary") })
	AccountResponse toDto(AccountEntity entity);

	List<AccountResponse> toDtoList(List<AccountEntity> entityList);
}
