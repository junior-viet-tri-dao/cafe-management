package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.MenuItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Integer> {

    @Query("Select p from MenuItemEntity p where p.isDeleted = false and LOWER(p.itemName) like lower(CONCAT('%', :keyword, '%'))")
    Page<MenuItemEntity> getAllMenuPageSearch(@Param("keyword") String keyword, Pageable pageable);

    @Query("Select p from MenuItemEntity p where p.isDeleted = false ")
    Page<MenuItemEntity> getAllMenuPage(Pageable pageable);

    @Query("Select p from MenuItemEntity p where p.isDeleted = false")
    List<MenuItemEntity> getAllMenuItems();
}
