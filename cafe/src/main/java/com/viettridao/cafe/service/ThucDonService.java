package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.menu.MenuDTO;
import com.viettridao.cafe.dto.menu.MenuDetailDTO;
import com.viettridao.cafe.model.ThucDon;

import java.util.List;

public interface ThucDonService {
    List<MenuDTO> getAllMenus();
    ThucDon createMenu(MenuDetailDTO menuDetailDTO);
}
