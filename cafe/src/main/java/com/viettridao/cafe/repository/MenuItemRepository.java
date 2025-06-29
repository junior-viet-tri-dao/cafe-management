package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.MenuItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {
    Page<MenuItemEntity> findAllByIsDeletedFalseAndItemNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<MenuItemEntity> findAllByIsDeletedFalse(Pageable pageable);
}
