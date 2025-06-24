package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.model.EmployeeEntity;

public interface EmployeeService {
    List<EmployeeEntity> getAllEmployees();

    EmployeeEntity getEmployeeById(Integer id);
}
