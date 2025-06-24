package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.EquipmentEntity;

/**
 * Repository để thao tác với bảng dữ liệu Equipment trong cơ sở dữ liệu. Kế
 * thừa từ JpaRepository để sử dụng các phương thức CRUD mặc định.
 */
@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Integer> {
	/**
	 * Truy vấn danh sách tất cả các EquipmentEntity có thuộc tính isDeleted =
	 * false.
	 * 
	 * @return Danh sách các EquipmentEntity chưa bị xóa mềm.
	 */
	List<EquipmentEntity> findAllByIsDeletedFalse();
}