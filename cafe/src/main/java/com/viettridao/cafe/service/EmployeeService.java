package com.viettridao.cafe.service;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.controller.response.EmployeeProfileResponse;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.model.EmployeeEntity;

public interface EmployeeService {
	EmployeeProfileResponse getProfileByUsername(String username);

	void updateProfile(EmployeeProfileRequest request);

	EmployeeEntity findByUsername(String username);

	EmployeeProfileResponse convertToResponse(EmployeeEntity employee);

	EmployeeProfileDTO getProfileDTO(String username);
}