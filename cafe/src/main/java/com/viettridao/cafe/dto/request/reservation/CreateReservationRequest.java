package com.viettridao.cafe.dto.request.reservation;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateReservationRequest {
    @NotNull(message = "Id bàn không được để trống")
    @Min(value = 1, message = "Id bàn phải lớn hơn 0")
    private Integer tableId;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(min = 3, message = "Tên khách hàng tối thiểu 3 ký tự")
    private String customerName;

    @Pattern(regexp = "^0[0-9]{9,10}$", message = "Số điện thoại phải bắt đầu bằng số 0 và số điện thoại 10-11 chữ số")
    private String customerPhone;

    @NotNull(message = "Ngày đặt bàn không được để trống")
    @FutureOrPresent(message = "Ngày giờ đặt bàn không được nhỏ hơn hiện tại")
    private LocalDateTime reservationDate;
}
