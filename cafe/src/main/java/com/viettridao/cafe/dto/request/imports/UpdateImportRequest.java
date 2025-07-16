package com.viettridao.cafe.dto.request.imports;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class UpdateImportRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    @Positive(message = "ID sản phẩm phải là số dương") // Đảm bảo ID hợp lệ và lớn hơn 0
    private Integer id;


//    private String productName;

    @NotNull(message = "Ngày import không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate importDate;

    @NotNull(message = "quantity sản phẩm không được để trống")
    private Integer quantity;

    @NotNull(message = "totalAmount sản phẩm không được để trống")
    private Double totalAmount;

    @NotNull(message = "price sản phẩm không được để trống")
    private Double price;
}
