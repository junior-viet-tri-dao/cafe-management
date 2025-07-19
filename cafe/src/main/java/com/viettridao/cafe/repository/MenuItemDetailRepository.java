package com.viettridao.cafe.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuKey;

/**
 * MenuItemDetailRepository
 *
 * Version 1.0
 *
 * Date: 19-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 19-07-2025   mirodoan    Create
 *
 * Repository thao tác với chi tiết thực đơn (MenuDetailEntity).
 */
@Repository
public interface MenuItemDetailRepository extends JpaRepository<MenuDetailEntity, MenuKey> {

    /**
     * Xóa chi tiết thực đơn theo id của món ăn.
     *
     * @param menuItemId id của món ăn
     */
    void deleteByMenuItem_Id(Integer menuItemId);
}