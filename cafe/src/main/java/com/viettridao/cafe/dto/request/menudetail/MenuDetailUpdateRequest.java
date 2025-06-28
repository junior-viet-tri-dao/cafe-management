package com.viettridao.cafe.dto.request.menudetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDetailUpdateRequest {
    private Integer menuItemId;
    private Integer productId;
    private Double quantity;
    private String unitName;
    private Boolean deleted;
}
