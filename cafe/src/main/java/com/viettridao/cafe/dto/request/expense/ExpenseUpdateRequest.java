package com.viettridao.cafe.dto.request.expense;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpenseUpdateRequest {
    @NotNull(message = "Id chi tiêu không được để trống")
    @Min(value = 1, message = "Id chi tiêu phải lớn hơn 0")
    private Integer id;

    @NotNull(message = "Giá tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá tiền phải lớn hơn 0")
    private Double amount;

    @NotBlank(message = "Tên chi tiêu không được để trống")
    @Size(min = 3, message = "Tên chi tiêu tối thiểu 3 ký tự")
    private String expenseName;

    @NotNull(message = "Ngày chi tiêu không được để trống")
    @PastOrPresent(message = "Ngày chi tiêu không được lớn hơn ngày hiện tại")
    private LocalDate expenseDate;

    private Integer accountId;
}