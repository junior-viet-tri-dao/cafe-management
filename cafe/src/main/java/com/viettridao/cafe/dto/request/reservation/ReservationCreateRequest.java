package com.viettridao.cafe.dto.request.reservation;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Getter
@Setter
public class ReservationCreateRequest {

    @NotNull(message = "Bàn không được để trống")
    private Integer tableId;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(max = 100, message = "Tên khách hàng không được quá 100 ký tự")
    private String customerName;

    @NotBlank(message = "SĐT không được để trống")
    @Pattern(regexp = "0[0-9]{9,10}", message = "Số điện thoại phải bắt đầu bằng 0 và có 10-11 số")
    private String customerPhone;

    @NotNull(message = "Thời gian đặt bàn không được để trống")
    @Future(message = "Thời gian đặt bàn phải là tương lai")
    private LocalDateTime reservationDate;

    private Boolean deleted = false;
}
