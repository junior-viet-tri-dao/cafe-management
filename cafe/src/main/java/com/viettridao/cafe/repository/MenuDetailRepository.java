package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuKey;

public interface MenuDetailRepository extends JpaRepository<MenuDetailEntity, MenuKey> {
    
    // Tìm tất cả các thành phần theo ID món
    List<MenuDetailEntity> findByMenuItem_IdAndIsDeletedFalse(Integer menuItemId);
}
