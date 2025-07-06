package com.viettridao.cafe.dto.request.expense;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExpenseUpdateRequest {

    @NotNull(message = "ID không được để trống")
    private Integer id;

    @NotNull(message = "Số tiền không được để trống")
    @Positive(message = "Số tiền phải lớn hơn 0")
    private Double amount;

    @NotBlank(message = "Tên chi phí không được để trống")
    private String expenseName;

    @NotNull(message = "Ngày chi không được để trống")
    @PastOrPresent(message = "Ngày chi không được vượt quá ngày hôm nay")
    private LocalDate expenseDate;

    private Integer accountId;

}
