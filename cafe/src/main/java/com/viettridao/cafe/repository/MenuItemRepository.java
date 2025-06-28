package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.MenuItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {
    @Query("select m from MenuItemEntity m where m.isDeleted = false and lower(m.itemName) like lower(CONCAT('%', :keyword, '%')) ")
    Page<MenuItemEntity> getAllByMenuItems(@Param("keyword") String keyword, Pageable pageable);

    @Query("select m from MenuItemEntity m where m.isDeleted = false")
    Page<MenuItemEntity> getAllByMenuItems(Pageable pageable);
}
