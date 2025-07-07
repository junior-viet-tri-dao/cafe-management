package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
