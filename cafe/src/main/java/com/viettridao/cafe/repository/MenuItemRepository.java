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

	Page<MenuItemEntity> findByIsDeletedFalseAndItemNameContainingIgnoreCase(String keyword, Pageable pageable);

	boolean existsByItemNameAndIsDeletedFalse(String itemName);

	List<MenuItemEntity> findByIsDeletedFalse();

	Optional<MenuItemEntity> findByItemNameIgnoreCaseAndIsDeletedFalse(String itemName);
}
