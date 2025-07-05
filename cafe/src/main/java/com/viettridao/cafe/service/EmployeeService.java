package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeePageResponse;
import com.viettridao.cafe.model.EmployeeEntity;

public interface EmployeeService {
	EmployeePageResponse getAllEmployees(String keyword, int page, int size);

	EmployeeEntity createEmployee(CreateEmployeeRequest request);

	void deleteEmployee(Integer id);

	void updateEmployee(UpdateEmployeeRequest request);

	EmployeeEntity getEmployeeById(Integer id);
}
