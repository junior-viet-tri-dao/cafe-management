package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.controller.response.EmployeeProfileResponse;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.model.EmployeeEntity;

/**
 * Giao diện định nghĩa các dịch vụ nghiệp vụ liên quan đến quản lý nhân viên.
 * Bao gồm các chức năng như xem, cập nhật, tạo, xóa và tìm kiếm thông tin nhân
 * viên.
 */
public interface EmployeeService {
	/**
	 * Lấy thông tin hồ sơ nhân viên dưới dạng phản hồi (Response) dựa trên tên đăng
	 * nhập.
	 *
	 * @param username Tên đăng nhập của nhân viên.
	 * @return Đối tượng EmployeeProfileResponse chứa thông tin hồ sơ.
	 */
	EmployeeProfileResponse getProfileByUsername(String username);

	/**
	 * Cập nhật thông tin hồ sơ nhân viên dựa trên dữ liệu từ yêu cầu (Request).
	 *
	 * @param request Đối tượng EmployeeProfileRequest chứa thông tin cần cập nhật.
	 */
	void updateProfile(EmployeeProfileRequest request);

	/**
	 * Tìm kiếm một thực thể nhân viên (EmployeeEntity) dựa trên tên đăng nhập.
	 *
	 * @param username Tên đăng nhập của nhân viên.
	 * @return Đối tượng EmployeeEntity tìm thấy.
	 */
	EmployeeEntity findByUsername(String username);

	/**
	 * Chuyển đổi một thực thể EmployeeEntity sang đối tượng phản hồi
	 * EmployeeProfileResponse.
	 *
	 * @param employee Thực thể EmployeeEntity cần chuyển đổi.
	 * @return Đối tượng EmployeeProfileResponse đã được chuyển đổi.
	 */
	EmployeeProfileResponse convertToResponse(EmployeeEntity employee);

	/**
	 * Tạo một nhân viên mới dựa trên dữ liệu từ yêu cầu (Request).
	 *
	 * @param request Đối tượng EmployeeProfileRequest chứa thông tin để tạo nhân
	 *                viên.
	 */
	void create(EmployeeProfileRequest request);

	/**
	 * Xóa một nhân viên theo ID của họ (thường là xóa mềm - soft delete).
	 *
	 * @param id ID của nhân viên cần xóa.
	 */
	void deleteById(Integer id);

	/**
	 * Lấy danh sách tất cả các nhân viên đang hoạt động (chưa bị xóa logic).
	 *
	 * @return Một List các EmployeeProfileResponse của các nhân viên hoạt động.
	 */
	List<EmployeeProfileResponse> getAllActive();

	/**
	 * Tìm kiếm nhân viên dựa trên một từ khóa nhất định.
	 *
	 * @param keyword Từ khóa dùng để tìm kiếm (ví dụ: theo tên, số điện thoại,
	 *                v.v.).
	 * @return Một List các EmployeeProfileResponse khớp với từ khóa tìm kiếm.
	 */
	List<EmployeeProfileResponse> search(String keyword);

	/**
	 * Lấy một thực thể nhân viên (EmployeeEntity) theo ID của họ.
	 *
	 * @param id ID của nhân viên cần tìm.
	 * @return Đối tượng EmployeeEntity tìm thấy.
	 */
	EmployeeEntity getById(Integer id);

	/**
	 * Lấy thông tin hồ sơ nhân viên dưới dạng DTO (Data Transfer Object) dựa trên
	 * tên đăng nhập.
	 *
	 * @param username Tên đăng nhập của nhân viên.
	 * @return Đối tượng EmployeeProfileDTO chứa thông tin hồ sơ.
	 */
	EmployeeProfileDTO getProfileDTO(String username);
}