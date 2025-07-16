package com.viettridao.cafe.service;


import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponsePage;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PositionService {

    PositionEntity getPositionByID(Integer id);

    List<PositionEntity> getPosition();

}
