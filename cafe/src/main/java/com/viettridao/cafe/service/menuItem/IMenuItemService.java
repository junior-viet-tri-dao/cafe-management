package com.viettridao.cafe.service.menuItem;

import java.util.List;

import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.dto.response.menuitem.MenuItemResponse;

public interface IMenuItemService {

    List<MenuItemResponse> getMenuItemAll();

    void createMenuItem(MenuItemCreateRequest request);

    MenuItemUpdateRequest getUpdateForm(Integer id);

    void updateMenuItem(Integer id, MenuItemUpdateRequest request);

    void deleteMenuItem(Integer id);

}
