package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.ImportResponse;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.model.ImportEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final ModelMapper modelMapper;

    public EmployeeResponse toEmployeeRespones(EmployeeEntity entity){
        EmployeeResponse response = new EmployeeResponse();
        modelMapper.map(entity,response);
                if(entity.getPosition() != null){
                    response.setPositionId(entity.getPosition().getId());
                    response.setPositionName(entity.getPosition().getPositionName());
                    response.setSalary(entity.getPosition().getSalary());

                }

                if(entity.getAccount() != null) {
                    response.setUsername(entity.getAccount().getUsername());
                    response.setPassword(entity.getAccount().getPassword());
                    response.setImageUrl(entity.getAccount().getImageUrl());
                }

                return response;
    }

    public List<EmployeeResponse> toListEmployeeResponse(List<EmployeeEntity> entities){
        return entities.stream().map(this::toEmployeeRespones).toList();
    }




}
