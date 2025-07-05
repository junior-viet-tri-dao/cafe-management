package com.viettridao.cafe.dto.response.menu;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemPageResponse {

	private List<MenuItemResponse> items;

	private int currentPage;

	private int totalPages;

	private long totalItems;
	
}
