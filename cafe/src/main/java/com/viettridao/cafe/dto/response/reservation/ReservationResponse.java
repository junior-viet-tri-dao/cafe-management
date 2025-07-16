package com.viettridao.cafe.dto.response.reservation;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.response.PageResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReservationResponse {

    private Integer tableId;

    private String tableName;

    private TableStatus status;

    private List<MenuItemReservationResponse> menuItems;

    private Integer quantity;

    private Double totalPrice;

    private String customerName;

    private String customerPhone;

    private LocalDateTime reservationDate;
}
