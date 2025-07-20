package com.viettridao.cafe.dto.request.export;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExportUpdateRequest {

    @NotNull(message = "ID không được để trống")
    private Integer id;

    @NotNull(message = "Ngày xuất không được để trống")
    @PastOrPresent(message = "Ngày xuất không được vượt quá ngày hôm nay")
    private LocalDate exportDate;

    @NotNull(message = "Tổng tiền không được để trống")
    @Positive(message = "Tổng tiền phải lớn hơn 0")
    private Double totalAmount;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng tối thiểu là 1")
    private Integer quantity;

    private Integer employeeId;

    @NotNull(message = "Sản phẩm không được để trống")
    private Integer productId;

}
