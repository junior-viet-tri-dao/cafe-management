package com.viettridao.cafe.dto.request.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDetailRequest {
	private String productName;
	private Double quantity;
	private String unitName;
}
