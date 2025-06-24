package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.viettridao.cafe.dto.request.employee.AddEmployeeRequest;
import com.viettridao.cafe.model.EmployeeEntity;

public interface EmployeeService {
    List<EmployeeEntity> getAllEmployees();

    EmployeeEntity getEmployeeById(Integer id);

    void createEmployee(AddEmployeeRequest request, MultipartFile avatar);
}
