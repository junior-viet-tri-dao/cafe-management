package com.viettridao.cafe.dto.response.menu;

import com.viettridao.cafe.dto.response.PageResponse;
import com.viettridao.cafe.model.MenuItemEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItemResponsePage extends PageResponse {

    private List<MenuItemEntity> menuPage;
}
