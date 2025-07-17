package com.viettridao.cafe.dto.request.reservation;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {
    @NotNull(message = "Bàn không được để trống")
    private Integer tableId;

    @NotBlank(message = "Tên khách hàng không được để trống")
    private String customerName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "\\d{9,11}", message = "Số điện thoại không hợp lệ")
    private String customerPhone;

    @NotNull(message = "Ngày đặt không được để trống")
    @Future(message = "Ngày đặt phải ở tương lai")
    private LocalDateTime reservationDate;

    private Boolean isDeleted = false;
}
