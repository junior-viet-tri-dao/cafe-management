package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.MenuItemEntity;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {

    // ✅ Tìm món theo tên chứa từ khóa, không bị xóa (có phân trang)
    Page<MenuItemEntity> findByIsDeletedFalseAndItemNameContainingIgnoreCase(String keyword, Pageable pageable);

    // ✅ Kiểm tra món đã tồn tại chưa (tên trùng và chưa bị xóa)
    boolean existsByItemNameAndIsDeletedFalse(String itemName);

    // ✅ Tìm tất cả món chưa bị xóa (không phân trang)
    List<MenuItemEntity> findByIsDeletedFalse();

    // ✅ Tìm món theo tên chính xác (không phân biệt hoa thường), chưa bị xóa
    Optional<MenuItemEntity> findByItemNameIgnoreCaseAndIsDeletedFalse(String itemName);
}
