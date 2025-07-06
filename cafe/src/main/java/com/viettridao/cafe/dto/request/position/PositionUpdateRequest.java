package com.viettridao.cafe.dto.request.position;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionUpdateRequest {

    @NotNull(message = "ID không được để trống")
    private Integer id;

    @NotBlank(message = "Tên chức vụ không được để trống")
    private String positionName;

    @NotNull(message = "Lương không được để trống")
    @Positive(message = "Lương phải lớn hơn 0")
    private Double salary;

}
