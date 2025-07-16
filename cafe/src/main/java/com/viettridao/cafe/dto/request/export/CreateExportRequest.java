package com.viettridao.cafe.dto.request.export;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreateExportRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    @Positive(message = "ID sản phẩm phải là số nguyên dương")
    private Integer product_id;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng xuất phải là số nguyên dương") // Số lượng xuất thường phải > 0
    private Integer quantity;

    @NotNull(message = "Ngày xuất không được để trống")
    @PastOrPresent(message = "Ngày xuất không được ở tương lai") // Ngày xuất phải là hôm nay hoặc trước đó
    private LocalDate exportDate;
}
