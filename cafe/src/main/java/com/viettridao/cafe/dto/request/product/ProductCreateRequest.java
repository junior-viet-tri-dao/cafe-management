package com.viettridao.cafe.dto.request.product;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {

    @NotBlank(message = "Tên hàng hóa không được để trống")
    private String productName;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng không được âm")
    private Integer quantity = 0;

    private Integer unitId;

}
