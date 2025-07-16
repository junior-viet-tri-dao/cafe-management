package com.viettridao.cafe.dto.request.menu;

import com.viettridao.cafe.dto.request.menudetail.CreateMenuItemDetailRequest;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMenuItemRequest {

    @NotBlank(message = "Tên món ăn không được để trống")
    private String itemName;

    @NotNull(message = "Giá hiện tại không được để trống")
    @PositiveOrZero(message = "Giá hiện tại phải là số không âm") // Cho phép giá là 0 (ví dụ: món tặng kèm)
    // Hoặc @Positive(message = "Giá hiện tại phải là số dương") nếu giá luôn phải lớn hơn 0
    private Double currentPrice;

    @NotNull(message = "Chi tiết thực đơn không được để trống")
    private List<CreateMenuItemDetailRequest> menuItemDetail;
}
