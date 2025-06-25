package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viettridao.cafe.model.EmployeeEntity;

/**
 * Giao diện Repository để thao tác với bảng dữ liệu nhân viên (employees) trong
 * cơ sở dữ liệu. Kế thừa từ JpaRepository để sử dụng các phương thức CRUD
 * (Create, Read, Update, Delete) mặc định. Cung cấp các phương thức tìm kiếm
 * tùy chỉnh cho thực thể EmployeeEntity.
 */
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {

	/**
	 * Tìm kiếm một nhân viên dựa trên tên đăng nhập của tài khoản liên kết với nhân
	 * viên đó. Spring Data JPA sẽ tự động tạo truy vấn thông qua mối quan hệ từ
	 * EmployeeEntity đến AccountEntity và tìm theo trường 'username'.
	 *
	 * @param username Tên đăng nhập của tài khoản cần tìm.
	 * @return Một `Optional` chứa `EmployeeEntity` nếu tìm thấy, hoặc
	 *         `Optional.empty()` nếu không tìm thấy.
	 */
	Optional<EmployeeEntity> findByAccount_Username(String username);

	/**
	 * Lấy tất cả danh sách nhân viên có trạng thái `isDeleted` là `false` (nghĩa là
	 * chưa bị xóa logic).
	 *
	 * @return Một `List` các `EmployeeEntity` đang hoạt động.
	 */
	List<EmployeeEntity> findAllByIsDeletedFalse();

	/**
	 * Tìm kiếm danh sách nhân viên chưa bị xóa logic, trong đó tên đầy đủ
	 * (`fullName`) hoặc tên chức vụ (`position.positionName`) chứa từ khóa tìm kiếm
	 * (không phân biệt chữ hoa, chữ thường).
	 *
	 * @param name     Từ khóa để so khớp với tên đầy đủ của nhân viên.
	 * @param position Từ khóa để so khớp với tên chức vụ của nhân viên.
	 * @return Một `List` các `EmployeeEntity` phù hợp với tiêu chí tìm kiếm.
	 */
	List<EmployeeEntity> findByIsDeletedFalseAndFullNameContainingIgnoreCaseOrPosition_PositionNameContainingIgnoreCase(
			String name, String position);

	/**
	 * Tìm kiếm danh sách nhân viên chưa bị xóa logic và có mức lương cụ thể.
	 *
	 * @param salary Mức lương cần tìm.
	 * @return Một `List` các `EmployeeEntity` có mức lương khớp.
	 */
	List<EmployeeEntity> findByIsDeletedFalseAndPosition_Salary(Double salary);
}