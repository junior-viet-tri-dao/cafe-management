package com.viettridao.cafe.dto.response.employee;

import com.viettridao.cafe.dto.response.ResponsePage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmployeeResponse {

    private Integer id;

    private String fullName;

    private String phoneNumber;

    private String address;

    private String imageUrl;

    private Integer positionId;

    private String positionName;

    private Double salary;

    private String username;

    private String password;
}
