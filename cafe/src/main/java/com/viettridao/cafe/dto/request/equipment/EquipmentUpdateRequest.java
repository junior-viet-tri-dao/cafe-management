package com.viettridao.cafe.dto.request.equipment;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class EquipmentUpdateRequest {

    @NotNull(message = "ID không được để trống")
    private Integer id;

    @NotBlank(message = "Tên thiết bị không được để trống")
    private String equipmentName;

    private String notes;

    @NotNull(message = "Ngày mua không được để trống")
    @PastOrPresent(message = "Ngày mua không được ở tương lai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    @NotNull(message = "Giá mua không được để trống")
    @Positive(message = "Giá mua phải lớn hơn 0")
    private Double purchasePrice;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải từ 1 trở lên")
    private Integer quantity;

}
