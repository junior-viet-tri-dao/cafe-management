package com.viettridao.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileDTO {
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
