package com.viettridao.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp DTO (Data Transfer Object) dùng để truyền tải thông tin hồ sơ nhân viên.
 * Chứa các thuộc tính liên quan đến thông tin cá nhân và tài khoản của nhân
 * viên.
 */

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
