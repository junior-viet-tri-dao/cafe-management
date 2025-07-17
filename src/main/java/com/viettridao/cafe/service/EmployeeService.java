package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.viettridao.cafe.dto.request.employee.AddEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.SearchEmployeeResponse;
import com.viettridao.cafe.model.EmployeeEntity;

public interface EmployeeService {

    Page<SearchEmployeeResponse> getAllEmployees(Pageable pageable);

    EmployeeEntity getEmployeeById(Integer id);

    void createEmployee(AddEmployeeRequest request, MultipartFile avatar);

    List<SearchEmployeeResponse> searchByName(String keyword);

    List<EmployeeEntity> getAllEmployeeDescSalary();
}
