package com.viettridao.cafe.dto.request.expense;

import java.time.LocalDate;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpenseCreateRequest {

    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private Double amount;

    @NotBlank(message = "Tên chi phí không được để trống")
    private String expenseName;

    @NotNull(message = "Ngày chi không được để trống")
    @PastOrPresent(message = "Ngày chi không được vượt quá ngày hôm nay")
    private LocalDate expenseDate;


}
