package com.viettridao.cafe.dto.response.imports;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO cho việc biểu diễn dữ liệu liên quan đến nhập hàng.
 * Bao gồm thông tin về ngày nhập, sản phẩm, giá, đơn vị và số lượng.
 */
@Getter
@Setter
public class ImportResponse {
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate importDate;

    private String productName;

    private Integer productId;

    private Double productPrice;

    private String unitName;

    private Integer quantity;
}
