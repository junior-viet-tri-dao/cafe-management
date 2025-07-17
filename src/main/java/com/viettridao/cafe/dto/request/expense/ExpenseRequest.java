package com.viettridao.cafe.dto.request.expense;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseRequest {
    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private Double amount;

    @NotBlank(message = "Tên chi tiêu không được để trống")
    private String expenseName;

    @NotNull(message = "Ngày chi tiêu không được để trống")
    private LocalDate expenseDate;

    private Boolean isDeleted = false;
}