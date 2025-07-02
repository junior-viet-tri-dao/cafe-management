package com.viettridao.cafe.dto.request.tables;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemSplitRequest {
    private Integer menuItemId;     // ID món trong hóa đơn
    private Integer quantityToMove; // Số lượng muốn chuyển
    private Boolean selected;
}

