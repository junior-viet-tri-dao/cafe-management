package com.viettridao.cafe.dto.request.equipment;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEquipmentRequest {

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(min = 2, max = 100, message = "Tên thiết bị phải từ 2 đến 100 ký tự")
    private String equipmentName;

    @NotNull(message = "Giá mua không được để trống")
    @DecimalMin(value = "1000.0", message = "Giá mua phải ít nhất 1.000 VNĐ")
    @DecimalMax(value = "1000000000.0", message = "Giá mua không được quá 1 tỷ VNĐ")
    private BigDecimal purchasePrice;

    private BigDecimal totalAmount;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    private Boolean isDeleted = false;

    @Size(max = 255, message = "Ghi chú không được quá 255 ký tự")
    private String notes;

    @NotNull(message = "Ngày mua không được để trống")
    @PastOrPresent(message = "Ngày mua không được lớn hơn ngày hiện tại")
    private LocalDate purchaseDate;
}
