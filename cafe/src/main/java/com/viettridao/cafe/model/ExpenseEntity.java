package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "expenses")//chitieu
public class ExpenseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Integer id;

    @NotNull(message = "Số tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Số tiền phải lớn hơn 0")
    @Column(name = "amount")
    private Double amount;

    @NotBlank(message = "Tên chi phí không được để trống")
    @Size(max = 100, message = "Tên chi phí không được vượt quá 100 ký tự")
    @Column(name = "expense_name")
    private String expenseName;

    @NotNull(message = "Ngày chi phí không được để trống")
    @PastOrPresent(message = "Ngày chi phí phải là ngày hiện tại hoặc trong quá khứ")
    @Column(name = "expense_date")
    private LocalDate expenseDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "Tài khoản không được để trống")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private AccountEntity account;
}
