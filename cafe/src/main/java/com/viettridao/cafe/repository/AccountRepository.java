package com.viettridao.cafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.AccountEntity;

/**
 * Repository để thao tác với bảng dữ liệu Account trong cơ sở dữ liệu. Kế thừa
 * từ JpaRepository để sử dụng các phương thức CRUD mặc định.
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {

	/**
	 * Tìm kiếm một tài khoản dựa trên tên đăng nhập.
	 * 
	 * @param username Tên đăng nhập của tài khoản cần tìm
	 * @return Optional chứa AccountEntity nếu tìm thấy, hoặc rỗng nếu không tồn tại
	 */
	Optional<AccountEntity> findByUsername(String username);
}
