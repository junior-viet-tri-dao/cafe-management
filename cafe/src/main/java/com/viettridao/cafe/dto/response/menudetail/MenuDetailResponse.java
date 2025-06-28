package com.viettridao.cafe.dto.response.menudetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDetailResponse {

    private Integer menuItemId;

    private Integer productId;

    private Double quantity;

    private String unitName;

    private Boolean deleted;
}
