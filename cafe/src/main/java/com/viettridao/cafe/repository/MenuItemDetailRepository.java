package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemDetailRepository extends JpaRepository<MenuDetailEntity, MenuKey> {
}
