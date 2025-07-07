package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Thực thể Import lưu thông tin đơn nhập hàng.
 */
@Getter
@Setter
@Entity
@Table(name = "imports") // Bảng lưu thông tin đơn nhập hàng
public class ImportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imports_id") // Khóa chính của bảng
    private Integer id;

    @Column(name = "import_date") // Ngày nhập hàng
    private LocalDate importDate;

    @Column(name = "total_amount") // Tổng số tiền nhập hàng
    private Double totalAmount;

    @Column(name = "quantity") // Số lượng hàng nhập
    private Integer quantity;

    @Column(name = "is_deleted") // Trạng thái xóa mềm (soft delete)
    private Boolean isDeleted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id") // Nhân viên thực hiện đơn nhập
    private EmployeeEntity employee;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id") // Sản phẩm được nhập
    private ProductEntity product;
}