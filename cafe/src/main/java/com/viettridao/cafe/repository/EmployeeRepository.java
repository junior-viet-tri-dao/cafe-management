package com.viettridao.cafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viettridao.cafe.model.EmployeeEntity;

/**
 * Repository để thao tác với bảng dữ liệu Account trong cơ sở dữ liệu. Kế thừa
 * từ JpaRepository để sử dụng các phương thức CRUD mặc định.
 */
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
	/**
	 * Tìm kiếm một nhân viên dựa trên tên đăng nhập của tài khoản liên kết.
	 * 
	 * @param username Tên đăng nhập của tài khoản liên quan đến nhân viên
	 * @return Optional chứa EmployeeEntity nếu tìm thấy, hoặc rỗng nếu không tồn
	 *         tại
	 */
	Optional<EmployeeEntity> findByAccount_Username(String username);
}
