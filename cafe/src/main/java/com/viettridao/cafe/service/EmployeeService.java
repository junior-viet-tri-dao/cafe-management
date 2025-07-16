package com.viettridao.cafe.service;


import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.EmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponsePage;
import com.viettridao.cafe.model.EmployeeEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {

    EmployeeResponsePage getAllEmployees(String keyword, int page, int size);
    EmployeeEntity createEmployee(CreateEmployeeRequest request);
    boolean deleteEmployee(Integer id);
    void updateEmployee(UpdateEmployeeRequest request);
    EmployeeEntity getEmployeeById(Integer id);
    List<EmployeeEntity> findEmployeeByIsDeletedFalse();


}
