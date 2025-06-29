package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.viettridao.cafe.model.MenuItemEntity;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {

	// Kiểm tra trùng tên món (trong các món chưa xóa)
	boolean existsByItemNameAndIsDeletedFalse(String itemName);

	// Lấy món theo ID (chưa bị xóa mềm)
	Optional<MenuItemEntity> findByIdAndIsDeletedFalse(Integer id);

	// Danh sách món chưa xóa (không phân trang)
	List<MenuItemEntity> findAllByIsDeletedFalse();

	// Tìm kiếm theo tên (không phân biệt hoa thường, chưa xóa)
	List<MenuItemEntity> findByItemNameContainingIgnoreCaseAndIsDeletedFalse(String keyword);

	// Danh sách món chưa xóa (phân trang)
	Page<MenuItemEntity> findAllByIsDeletedFalse(Pageable pageable);

	// Tìm kiếm theo tên có phân trang
	Page<MenuItemEntity> findByItemNameContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);
}
