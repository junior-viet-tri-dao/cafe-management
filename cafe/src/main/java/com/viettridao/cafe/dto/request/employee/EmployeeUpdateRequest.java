package com.viettridao.cafe.dto.request.employee;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeUpdateRequest {

    @NotNull(message = "ID không được để trống")
    private Integer id;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private Integer accountId;

    @NotNull(message = "Chức vụ không được để trống")
    private Integer positionId;

    private Double salary;

}
