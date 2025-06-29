package com.viettridao.cafe.dto.request.menu;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemDetailCreateRequest {
    @NotNull(message = "Id hàng hóa chi tiết thực đơn không được để trống")
    @Min(value = 1, message = "Id hàng hóa chi tiết thực đơn phải lớn hơn 0")
    private Integer productId;

    @NotBlank(message = "Tên đơn vị thành phần không được để trống")
    private String unitName;

    @NotNull(message = "Khối lượng thành phần không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Khối lượng thành phần phải lớn hơn 0")
    private Double quantity;
}
