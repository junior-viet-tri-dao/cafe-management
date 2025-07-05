package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuKey;

@Repository
public interface MenuDetailRepository extends JpaRepository<MenuDetailEntity, MenuKey> {

	List<MenuDetailEntity> findByMenuItem_IdAndIsDeletedFalse(Integer menuItemId);
}
