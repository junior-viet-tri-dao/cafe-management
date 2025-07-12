package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.viettridao.cafe.model.MenuItemEntity;

/**
 * Repository thao tác với món ăn (MenuItemEntity)
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {

    /**
     * Tìm tất cả menu items không bị xóa mềm
     */
    List<MenuItemEntity> findByIsDeletedFalseOrIsDeletedIsNull();

    /**
     * Tìm tất cả menu items không bị xóa mềm với phân trang
     */
    @Query("select m from MenuItemEntity m where m.isDeleted = false and lower(m.itemName) like lower(CONCAT('%', :keyword, '%')) ")
    Page<MenuItemEntity> getAllByMenuItems(@Param("keyword") String keyword, Pageable pageable);

    @Query("select m from MenuItemEntity m where m.isDeleted = false")
    Page<MenuItemEntity> getAllByMenuItems(Pageable pageable);

    /**
     * Kiểm tra tên thực đơn đã tồn tại (không bị xóa mềm)
     */
    boolean existsByItemNameAndIsDeletedFalse(String itemName);

    /**
     * Kiểm tra trùng tên thực đơn, loại trừ chính nó (không tính món đang cập nhật)
     */
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MenuItemEntity m WHERE m.isDeleted = false AND lower(m.itemName) = lower(:itemName) AND m.id <> :menuItemId")
    boolean existsByItemNameAndIsDeletedFalseAndIdNot(@Param("itemName") String itemName,
            @Param("menuItemId") Integer menuItemId);

}
