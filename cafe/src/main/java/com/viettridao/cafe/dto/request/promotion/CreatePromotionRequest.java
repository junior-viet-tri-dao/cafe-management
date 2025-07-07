package com.viettridao.cafe.dto.request.promotion;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO cho việc tạo mới thông tin khuyến mãi.
 * Bao gồm các trường như tên khuyến mãi, ngày bắt đầu, ngày kết thúc và giá trị
 * giảm giá.
 */
@Getter
@Setter
public class CreatePromotionRequest {
    @NotBlank(message = "Tên khuyến mãi không được để trống")
    @Size(min = 5, message = "Tên khuyến mãi tối thiểu 5 ký tự")
    private String promotionName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @PastOrPresent(message = "Ngày bắt đầu không được lớn hơn ngày hiện tại")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải lớn hơn ngày hiện tại")
    private LocalDate endDate;

    @NotNull(message = "Phần trăm giảm không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Phần trăm giảm phải lớn hơn 0")
    private Double discountValue;
}
