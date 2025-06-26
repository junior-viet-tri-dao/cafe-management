package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.EquipmentEntity;

/**
 * Giao diện Repository để thao tác với bảng dữ liệu Equipment trong cơ sở dữ
 * liệu. Kế thừa từ JpaRepository để sử dụng các phương thức CRUD mặc định
 * (Create, Read, Update, Delete).
 */
@Repository // Đánh dấu interface này là một Spring Data JPA Repository, cho phép Spring tự
			// động tìm và tạo bean
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Integer> {
	// Kế thừa các phương thức CRUD cơ bản như save(), findById(), findAll(),
	// delete()
	// cho thực thể EquipmentEntity, với khóa chính có kiểu dữ liệu là Integer.

	/**
	 * Truy vấn danh sách tất cả các EquipmentEntity có thuộc tính **`isDeleted`**
	 * là **`false`**. Phương thức này sẽ chỉ trả về những thiết bị chưa bị xóa
	 * logic (soft delete).
	 *
	 * @return Một **List** chứa các **EquipmentEntity** chưa bị xóa mềm.
	 */
	List<EquipmentEntity> findAllByIsDeletedFalse();
}