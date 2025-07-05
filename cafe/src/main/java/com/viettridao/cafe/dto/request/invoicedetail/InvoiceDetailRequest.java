package com.viettridao.cafe.dto.request.invoicedetail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDetailRequest {

    private Integer invoiceId;

    private Integer menuItemId;

    private Integer quantity;

    private Double price;

}
