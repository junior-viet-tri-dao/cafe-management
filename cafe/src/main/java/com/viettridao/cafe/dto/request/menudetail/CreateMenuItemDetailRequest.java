package com.viettridao.cafe.dto.request.menudetail;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMenuItemDetailRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    @Positive(message = "ID sản phẩm phải là số nguyên dương")
    private Integer productId;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải là số dương") // Giả sử số lượng thành phần phải > 0
    // Nếu số lượng có thể là 0, hãy dùng @PositiveOrZero(message = "Số lượng phải là số không âm")
    private Double quantity; // Đã sửa từ 'quanlity' thành 'quantity'

    @NotBlank(message = "Tên đơn vị không được để trống")
    private String unitName;

}
