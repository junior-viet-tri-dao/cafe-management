package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")//hanghoa
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 100, message = "Tên sản phẩm không được vượt quá 100 ký tự")
    @Column(name = "product_name")
    private String productName;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá sản phẩm không thể là số âm")
    @Column(name = "price")
    private float price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ImportEntity> imports;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ExportEntity> exports;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<MenuDetailEntity> menuDetails;

    @NotNull(message = "Đơn vị tính không được để trống")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id")
    private UnitEntity unit;
}
