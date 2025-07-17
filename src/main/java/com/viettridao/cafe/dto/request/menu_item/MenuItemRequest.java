package com.viettridao.cafe.dto.request.menu_item;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {
    @NotBlank(message = "Tên món không được để trống")
    private String itemName;

    @NotNull(message = "Giá tiền không được để trống")
    @Min(value = 0, message = "Giá tiền phải lớn hơn hoặc bằng 0")
    private BigDecimal currentPrice;

    private Boolean isDeleted = false;

    private List<MenuDetailRequest> menuDetails;
}
