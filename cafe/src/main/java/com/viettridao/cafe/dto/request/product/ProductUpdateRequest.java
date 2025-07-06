package com.viettridao.cafe.dto.request.product;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateRequest {

    @NotNull(message = "ID không được để trống")
    private Integer id;

    @NotBlank(message = "Tên hàng hóa không được để trống")
    private String productName;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không được âm")
    private Integer quantity;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private Double price;

    private Integer unitId;
}
