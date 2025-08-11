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
@Table(name = "exports")//donxuat
public class ExportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exports_id")
    private Integer id;

    @NotNull(message = "Tổng tiền xuất không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tổng tiền xuất không thể là số âm")
    @Column(name = "total_export_amount")
    private Double totalExportAmount;

    @NotNull(message = "Ngày xuất không được để trống")
    @PastOrPresent(message = "Ngày xuất phải là ngày hiện tại hoặc trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "export_date")
    private LocalDate exportDate;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "Nhân viên không được để trống")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @NotNull(message = "Sản phẩm không được để trống")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
}
