package com.viettridao.cafe.dto.request.Pay;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentItemRequest {
    private Integer menuItemId;
    private Integer quantity;
    private Double price;       
    private Double amount;
}

