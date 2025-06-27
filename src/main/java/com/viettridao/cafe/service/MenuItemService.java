package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.menu_item.CreateMenuItemRequest;
import com.viettridao.cafe.dto.response.menu_item.MenuItemResponse;

public interface MenuItemService {
    List<MenuItemResponse> getAllMenuItems();

    void deleteMenuItem(Integer id);

    void createMenu(CreateMenuItemRequest request);

    MenuItemResponse getMenuItemById(Integer id);
}
