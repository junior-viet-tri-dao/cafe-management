package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.menu.CreateMenuItemRequest;
import com.viettridao.cafe.dto.request.menu.UpdateMenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponsePage;
import com.viettridao.cafe.model.MenuItemEntity;
import jakarta.validation.Valid;

import java.util.List;


public interface MenuItemService {

    MenuItemEntity getMenuItemByID(Integer id);

    MenuItemResponsePage getAllMenuItemPage(String keyword, int page, int size);

    void createMenu(CreateMenuItemRequest createMenuItemRequest);




    void deleteMenuItemById(Integer id);

    void updateMenuItem(UpdateMenuItemRequest request);

    List<MenuItemEntity> getAllMenuItems();

    MenuItemEntity getMenuItemById(Integer menuId);
}
