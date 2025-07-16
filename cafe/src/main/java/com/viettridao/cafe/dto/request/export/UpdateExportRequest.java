package com.viettridao.cafe.dto.request.export;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateExportRequest {

    @NotNull(message = "ID sản phẩm không được để trống")
    @Positive(message = "ID sản phẩm phải là số dương") // Đảm bảo ID hợp lệ và lớn hơn 0
    private Integer id;

    @NotNull(message = "Ngày export không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate exportDate;

    @NotNull(message = "quantity sản phẩm không được để trống")
    private Integer quantity;

}
