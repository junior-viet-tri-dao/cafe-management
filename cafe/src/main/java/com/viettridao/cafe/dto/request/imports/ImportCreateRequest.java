package com.viettridao.cafe.dto.request.imports;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportCreateRequest {

    @NotNull(message = "Ngày nhập không được để trống")
    @PastOrPresent(message = "Ngày nhập không được vượt quá ngày hôm nay")
    private LocalDate importDate;

    @NotNull(message = "Tổng tiền không được để trống")
    @Positive(message = "Tổng tiền phải lớn hơn 0")
    private Double totalAmount;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng tối thiểu là 1")
    private Integer quantity;

    private Integer employeeId;

    @NotNull(message = "Vui lòng chọn sản phẩm")
    private Integer productId;

}
