package com.viettridao.cafe.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuKey;

/**
 * Repository thao tác với chi tiết thực đơn (MenuDetailEntity)
 */
@Repository
public interface MenuItemDetailRepository extends JpaRepository<MenuDetailEntity, MenuKey> {
    void deleteByMenuItem_Id(Integer menuItemId);
}
