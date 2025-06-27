package com.viettridao.cafe.dto.request.imports;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CreateImportRequest {
    @NotNull(message = "Id hàng hóa không được để trống")
    @Min(value = 1, message = "Id hàng hóa phải lớn hơn 0")
    private Integer productId;

    @NotNull(message = "Ngày nhập không được để trống")
    @PastOrPresent(message = "Ngày nhập không được lớn hơn ngày hiện tại")
    private LocalDate importDate;

    @NotNull(message = "Số lượng nhập không được để trống")
    @Min(value = 1, message = "Số lượng nhập phải lớn hơn 0")
    private Integer quantity;
}
