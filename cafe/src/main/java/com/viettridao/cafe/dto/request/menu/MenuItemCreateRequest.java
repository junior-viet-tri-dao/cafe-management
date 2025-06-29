package com.viettridao.cafe.dto.request.menu;

import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemCreateRequest {
    @NotBlank(message = "Tên thực đơn không được để trống")
    @Size(min = 3, message = "Tên thực đơn tối thiểu 3 ký tự")
    private String itemName;

    @NotNull(message = "Giá thực đơn không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá thực đơn phải lớn hơn 0")
    private Double currentPrice;

    @NotNull(message = "Chi tiết thực đơn không được để trống")
    private List<MenuItemDetailCreateRequest> details;
}