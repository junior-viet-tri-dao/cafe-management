package com.viettridao.cafe.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.AccountEntity;

@Component
public class AccountMapper extends BaseMapper<AccountEntity, Void, AccountResponse> {

	public AccountMapper(ModelMapper modelMapper) {
		super(modelMapper, AccountEntity.class, Void.class, AccountResponse.class);
	}

	@Override
	public AccountResponse toDto(AccountEntity entity) {
		AccountResponse res = super.toDto(entity);

		if (entity.getEmployee() != null) {
			var employee = entity.getEmployee();
			res.setFullName(employee.getFullName());
			res.setAddress(employee.getAddress());
			res.setPhoneNumber(employee.getPhoneNumber());

			if (employee.getPosition() != null) {
				var position = employee.getPosition();
				res.setPositionId(position.getId());
				res.setPositionName(position.getPositionName());
				res.setSalary(position.getSalary());
			}
		}

		return res;
	}

	@Override
	public List<AccountResponse> toDtoList(List<AccountEntity> entities) {
		return entities.stream().map(this::toDto).toList();
	}
}
