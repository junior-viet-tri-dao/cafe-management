package com.viettridao.cafe.dto.request.product;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {
    @NotBlank(message = "Tên hàng hóa không được để trống")
    @Size(min = 3, message = "Tên hàng hóa tối thiểu 3 ký tự")
    private String productName;

    @NotNull(message = "Giá hàng hóa không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá hàng hóa phải lớn hơn 0")
    private Double productPrice;

    @NotNull(message = "Id đơn vị tính không được để trống")
    @Min(value = 1, message = "Id đơn vị tính phải lớn hơn 0")
    private Integer unitId;
}
