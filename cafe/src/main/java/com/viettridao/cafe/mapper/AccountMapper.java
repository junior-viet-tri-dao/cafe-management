package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.model.AccountEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    private final ModelMapper modelMapper;

    public AccountResponse toAccountResponse(AccountEntity entity){
        AccountResponse response = new AccountResponse();
        modelMapper.map(entity, response);

        if(entity.getEmployee() != null){
            response.setFullName(entity.getEmployee().getFullName());
            response.setAddress(entity.getEmployee().getAddress());
            response.setPhoneNumber(entity.getEmployee().getPhoneNumber());
        }

        if(entity.getEmployee().getPosition() != null){
            response.setSalary(entity.getEmployee().getPosition().getSalary());
            response.setPositionName(entity.getEmployee().getPosition().getPositionName());
            response.setPositionId(entity.getEmployee().getPosition().getId());
        }
        return response;
    }

    public List<AccountResponse> toListAccountResponse(List<AccountEntity> entities) {
        return entities.stream()
                .map(this::toAccountResponse)
                .toList();
    }

}
