package com.viettridao.cafe.dto.response.menuitem;

import com.viettridao.cafe.dto.response.PageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItemPageResponse extends PageResponse {
    private List<MenuItemResponse> items;
}
