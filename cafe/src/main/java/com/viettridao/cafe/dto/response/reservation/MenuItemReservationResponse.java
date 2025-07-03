package com.viettridao.cafe.dto.response.reservation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemReservationResponse {
    private String itemName;

    private Integer quantity;

    private Double price;
}
