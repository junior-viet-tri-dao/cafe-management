package com.viettridao.cafe.dto.response.invoicedetail;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.response.invoice.InvoiceResponse;
import com.viettridao.cafe.dto.response.invoicekey.InvoiceKeyResponse;
import com.viettridao.cafe.dto.response.menuitem.MenuItemResponse;

@Getter
@Setter
public class InvoiceDetailResponse {

    private InvoiceKeyResponse id;

    private InvoiceResponse invoice;

    private MenuItemResponse menuItem;

    private Integer quantity;

    private Double price;

    private Boolean deleted;

}
