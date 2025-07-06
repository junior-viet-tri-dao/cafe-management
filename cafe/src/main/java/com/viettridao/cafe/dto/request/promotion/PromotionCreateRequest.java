package com.viettridao.cafe.dto.request.promotion;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Getter
@Setter
public class PromotionCreateRequest {

    @NotBlank(message = "Tên khuyến mãi không được để trống")
    private String promotionName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @PastOrPresent(message = "Ngày bắt đầu không được vượt quá ngày hiện tại")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @PastOrPresent(message = "Ngày kết thúc không được vượt quá ngày hiện tại")
    private LocalDate endDate;

    @NotNull(message = "Giá trị giảm không được để trống")
    @DecimalMin(value = "0.01", message = "Giá trị giảm phải lớn hơn 0")
    @DecimalMax(value = "100.00", message = "Giá trị giảm không vượt quá 100%")
    private Double discountValue;

    private Boolean status;

    private String description;

    private Boolean deleted = false;

    @AssertTrue(message = "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc")
    public boolean isValidDateRange() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }
}
