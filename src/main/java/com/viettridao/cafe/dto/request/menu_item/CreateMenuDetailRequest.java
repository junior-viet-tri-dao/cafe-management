package com.viettridao.cafe.dto.request.menu_item;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMenuDetailRequest {
    @NotNull(message = "Thành phần không được để trống")
    @Min(value = 1, message = "Thành phần không hợp lệ")
    private Integer productId;

    @NotNull(message = "Số lượng không được để trống")
    @DecimalMin(value = "0.01", message = "Số lượng phải lớn hơn 0")
    private Double quantity;

    @NotBlank(message = "Đơn vị không được để trống")
    private String unitName;

    private Boolean isDeleted = false;
}
