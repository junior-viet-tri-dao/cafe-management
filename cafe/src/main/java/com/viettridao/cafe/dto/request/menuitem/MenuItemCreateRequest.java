package com.viettridao.cafe.dto.request.menuitem;

import java.util.List;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.request.menudetail.MenuDetailCreateRequest;

@Getter
@Setter
public class MenuItemCreateRequest {

    @NotBlank(message = "Tên món không được để trống")
    private String itemName;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private Double currentPrice;

    @NotEmpty(message = "Chi tiết món không được để trống")
    private List<@Valid MenuDetailCreateRequest> menuDetails;
}
