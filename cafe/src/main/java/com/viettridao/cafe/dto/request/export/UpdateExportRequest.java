package com.viettridao.cafe.dto.request.export;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO cho việc cập nhật thông tin đơn xuất hàng.
 * Bao gồm các trường như ngày xuất, số lượng và thông tin sản phẩm.
 */
@Getter
@Setter
public class UpdateExportRequest {
    @NotNull(message = "Id đơn xuất không được để trống")
    @Min(value = 1, message = "Id phiếu xuất phải lớn hơn 0")
    private Integer Id;

    @NotNull(message = "Id hàng hóa không được để trống")
    @Min(value = 1, message = "Id hàng hóa phải lớn hơn 0")
    private Integer productId;

    @NotNull(message = "Ngày xuất không được để trống")
    @PastOrPresent(message = "Ngày xuất không được lớn hơn ngày hiện tại")
    private LocalDate exportDate;

    @NotNull(message = "Số lượng xuất không được để trống")
    @Min(value = 1, message = "Số xuất nhập phải lớn hơn 0")
    private Integer quantity;
}
