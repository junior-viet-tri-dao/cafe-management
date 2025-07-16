package com.viettridao.cafe.dto.request.imports;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class CreateImportRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    @Positive(message = "ID sản phẩm phải là số nguyên dương")
    private Integer product_id;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng nhập phải là số nguyên dương") // Số lượng nhập thường phải > 0
    private Integer quantity;

    @NotNull(message = "Đơn giá không được để trống")
    @Positive(message = "Đơn giá phải là số dương") // Giá thường phải > 0
    // Hoặc @Min(value = 0, message = "Đơn giá phải là số không âm") nếu 0 là giá trị hợp lệ
    private Double price; // Dùng Double thay float cho độ chính xác tốt hơn với tiền tệ

    @NotNull(message = "Ngày nhập không được để trống")
    @PastOrPresent(message = "Ngày nhập không được ở tương lai") // Ngày nhập phải là hôm nay hoặc trước đó
    private LocalDate importDate;

}
