package com.viettridao.cafe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.controller.response.EmployeeProfileResponse;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public EmployeeProfileResponse getProfileByUsername(String username) {
        return convertToResponse(findByUsername(username));
    }

    @Override
    public void updateProfile(EmployeeProfileRequest request) {
        EmployeeEntity employee = employeeRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

        employee.setFullName(request.getFullName());
        employee.setAddress(request.getAddress());
        employee.setPhoneNumber(request.getPhoneNumber());

        if (employee.getPosition() != null) {
            employee.getPosition().setPositionName(request.getPosition());
            employee.getPosition().setSalary(request.getSalary());
        }

        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            employee.getAccount().setImageUrl(request.getImageUrl());
        }

        employeeRepository.save(employee);
    }

    @Override
    public EmployeeEntity findByUsername(String username) {
        return employeeRepository.findByAccount_Username(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
    }

    @Override
    public EmployeeProfileResponse convertToResponse(EmployeeEntity employee) {
        EmployeeProfileResponse res = new EmployeeProfileResponse();
        res.setId(employee.getId());
        res.setFullName(employee.getFullName());
        res.setAddress(employee.getAddress());
        res.setPhoneNumber(employee.getPhoneNumber());

        if (employee.getPosition() != null) {
            res.setPosition(employee.getPosition().getPositionName());
            res.setSalary(employee.getPosition().getSalary());
        }

        if (employee.getAccount() != null) {
            res.setUsername(employee.getAccount().getUsername());
            res.setPassword(employee.getAccount().getPassword());
            res.setImageUrl(employee.getAccount().getImageUrl());
        }

        return res;
    }

    @Override
    public EmployeeProfileDTO getProfileDTO(String username) {
        EmployeeEntity employee = findByUsername(username);

        return new EmployeeProfileDTO(
                employee.getId(),
                employee.getFullName(),
                employee.getPosition() != null ? employee.getPosition().getPositionName() : null,
                employee.getAddress(),
                employee.getPhoneNumber(),
                employee.getPosition() != null ? employee.getPosition().getSalary() : null,
                employee.getAccount() != null ? employee.getAccount().getUsername() : null,
                employee.getAccount() != null ? employee.getAccount().getPassword() : null,
                employee.getAccount() != null ? employee.getAccount().getImageUrl() : null
        );
    }
}
