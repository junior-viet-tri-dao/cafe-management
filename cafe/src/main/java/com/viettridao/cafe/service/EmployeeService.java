package com.viettridao.cafe.service;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.controller.response.EmployeeProfileResponse;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.model.EmployeeEntity;

/**
 * Interface định nghĩa dịch vụ quản lý thông tin nhân viên. Cung cấp các phương
 * thức để lấy, cập nhật và chuyển đổi thông tin hồ sơ nhân viên.
 */
public interface EmployeeService {
	/**
	 * Lấy thông tin hồ sơ nhân viên dựa trên tên đăng nhập.
	 * 
	 * @param username Tên đăng nhập của nhân viên
	 * @return EmployeeProfileResponse chứa thông tin hồ sơ nhân viên
	 */
	EmployeeProfileResponse getProfileByUsername(String username);

	/**
	 * Cập nhật thông tin hồ sơ nhân viên dựa trên yêu cầu.
	 * 
	 * @param request Đối tượng chứa thông tin cần cập nhật
	 */
	void updateProfile(EmployeeProfileRequest request);

	/**
	 * Tìm nhân viên dựa trên tên đăng nhập.
	 * 
	 * @param username Tên đăng nhập của tài khoản liên kết
	 * @return EmployeeEntity nếu tìm thấy
	 */
	EmployeeEntity findByUsername(String username);

	/**
	 * Chuyển đổi thông tin nhân viên sang đối tượng response.
	 * 
	 * @param employee Thực thể nhân viên
	 * @return EmployeeProfileResponse chứa thông tin hồ sơ nhân viên
	 */
	EmployeeProfileResponse convertToResponse(EmployeeEntity employee);

	/**
	 * Lấy thông tin hồ sơ nhân viên dưới dạng DTO dựa trên tên đăng nhập.
	 * 
	 * @param username Tên đăng nhập của nhân viên
	 * @return EmployeeProfileDTO chứa thông tin hồ sơ nhân viên
	 */
	EmployeeProfileDTO getProfileDTO(String username);
}