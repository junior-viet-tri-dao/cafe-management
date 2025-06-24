package com.viettridao.cafe.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeProfileResponse {
	private Integer id;
	private String fullName;
	private String position;
	private String address;
	private String phoneNumber;
	private Double salary;
	private String username;
	private String password;
	private String imageUrl;
}