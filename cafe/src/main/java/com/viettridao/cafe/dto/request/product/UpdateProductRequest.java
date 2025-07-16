package com.viettridao.cafe.dto.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    @Positive(message = "ID sản phẩm phải là số dương") // Đảm bảo ID hợp lệ và lớn hơn 0
    private Integer id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

//    @NotNull(message = "Số lượng không được để trống")
//    private Integer quantity;
//
//    @NotNull(message = "Đơn giá không được để trống")
//    @Min(value = 0, message = "Đơn giá phải là số không âm") // Giá có thể là 0 trong một số trường hợp (ví dụ: sản phẩm tặng kèm)
//    private Float price; // Sử dụng Float hoặc Double cho tiền tệ là tốt, nhưng BigDecimal là tốt nhất cho độ chính xác cao.

}
