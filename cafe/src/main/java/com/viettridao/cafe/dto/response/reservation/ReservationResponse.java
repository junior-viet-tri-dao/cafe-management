package com.viettridao.cafe.dto.response.reservation;

import com.viettridao.cafe.common.TableStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReservationResponse {
    private String tableName;

    private TableStatus status;

    private List<MenuItemReservationResponse> menuItems;

    private Integer quantity;

    private Double totalPrice;

    private String customerName;

    private String customerPhone;

    private LocalDateTime reservationDate;
}
