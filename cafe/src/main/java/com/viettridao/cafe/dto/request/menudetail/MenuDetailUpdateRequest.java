package com.viettridao.cafe.dto.request.menudetail;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDetailUpdateRequest {

    private Integer menuItemId;

    @NotNull(message = "Nguyên liệu không được để trống")
    private Integer productId;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Double quantity;

    @NotBlank(message = "Đơn vị không được để trống")
    private String unitName;

    private Boolean deleted;
}
