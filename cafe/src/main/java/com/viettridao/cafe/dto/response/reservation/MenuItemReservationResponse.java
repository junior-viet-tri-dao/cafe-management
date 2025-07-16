package com.viettridao.cafe.dto.response.reservation;

import com.viettridao.cafe.common.TableStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MenuItemReservationResponse {

    private Integer id;

    private String itemName;

    private Integer quantity;

    private Double price;
}
