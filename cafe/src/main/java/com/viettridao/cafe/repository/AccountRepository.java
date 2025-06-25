package com.viettridao.cafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.AccountEntity;

/**
 * Repository để thao tác với bảng dữ liệu Account trong cơ sở dữ liệu. Kế thừa
 * từ JpaRepository để sử dụng các phương thức CRUD mặc định (Create, Read,
 * Update, Delete).
 */
@Repository // Đánh dấu interface này là một Spring Data JPA Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
	// Kế thừa các phương thức như save(), findById(), findAll(), delete() cho
	// AccountEntity với ID kiểu Integer

	/**
	 * Tìm kiếm một tài khoản dựa trên tên đăng nhập. Spring Data JPA sẽ tự động tạo
	 * triển khai cho phương thức này dựa trên tên.
	 *
	 * @param username Tên đăng nhập của tài khoản cần tìm.
	 * @return Optional chứa AccountEntity nếu tìm thấy tài khoản với tên đăng nhập
	 *         khớp, hoặc Optional rỗng nếu không tồn tại. Sử dụng Optional giúp
	 *         tránh lỗi NullPointerException.
	 */
	Optional<AccountEntity> findByUsername(String username);
}