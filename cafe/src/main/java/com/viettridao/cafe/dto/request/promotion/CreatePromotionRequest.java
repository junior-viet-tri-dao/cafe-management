package com.viettridao.cafe.dto.request.promotion;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatePromotionRequest {

    @NotBlank(message = "Tên khuyến mãi không được để trống")
    private String promotionName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là ngày hiện tại hoặc trong tương lai")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    // @Future(message = "Ngày kết thúc phải ở trong tương lai") // Nếu bạn muốn bắt buộc phải là ngày tương lai
    private LocalDate endDate;

    @NotNull(message = "Giá trị giảm giá không được để trống")
    @Min(value = 0, message = "Giá trị giảm giá phải là số không âm")
    private Double discountValue;
}
