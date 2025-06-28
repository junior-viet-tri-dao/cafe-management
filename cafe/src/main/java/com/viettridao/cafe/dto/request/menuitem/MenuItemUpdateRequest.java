package com.viettridao.cafe.dto.request.menuitem;

import com.viettridao.cafe.dto.request.menuitem_detail.MenuItemDetailCreateRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItemUpdateRequest {
    @NotNull(message = "Id thực đơn không được để trống")
    @Min(value = 1, message = "Id thực đơn phải lớn hơn 0")
    private Integer id;

    @NotBlank(message = "Tên thực đơn không được để trống")
    @Size(min = 3, message = "Tên thực đơn tối thiểu 3 ký tự")
    private String itemName;

    @NotNull(message = "Giá thực đơn không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá thực đơn phải lớn hơn 0")
    private Double currentPrice;

    @NotNull(message = "Chi tiết thực đơn không được để trống")
    private List<MenuItemDetailCreateRequest> details;
}
