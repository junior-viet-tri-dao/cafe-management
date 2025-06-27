package com.viettridao.cafe.dto.request.promotion;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePromotionRequest {

    @NotNull(message = "ID khuyến mãi không được để trống")
    private Integer id;

    @NotBlank(message = "Tên khuyến mãi không được để trống")
    @Size(min = 3, max = 100, message = "Tên khuyến mãi phải có từ 3 đến 100 ký tự")
    private String promotionName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc trong tương lai")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    @NotNull(message = "Giá trị giảm giá không được để trống")
    @DecimalMin(value = "0.1", message = "Giá trị giảm giá phải lớn hơn 0")
    @DecimalMax(value = "100.0", message = "Giá trị giảm giá không được vượt quá 100%")
    private Double discountValue;
}
