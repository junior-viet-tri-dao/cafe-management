package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "imports")//donnhap
public class ImportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imports_id")
    private Integer id;

    @NotNull(message = "Ngày nhập không được để trống")
    @PastOrPresent(message = "Ngày nhập phải là ngày hiện tại hoặc trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "import_date")
    private LocalDate importDate;

    @NotNull(message = "Tổng tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tổng tiền không thể là số âm")
    @Column(name = "total_amount")
    private Double totalAmount;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "Giá nhập không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá không thể là số âm")
    @Column(name = "price")
    private Double price;

    @NotNull(message = "Nhân viên không được để trống")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "equipment_id")
    private EquipmentEntity equipment;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
