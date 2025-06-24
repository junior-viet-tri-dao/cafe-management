package com.viettridao.cafe.controller.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Lớp DTO (Data Transfer Object) dùng để trả về thông tin hồ sơ nhân viên cho
 * client. Chứa các thuộc tính liên quan đến thông tin cá nhân và tài khoản của
 * nhân viên.
 */
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