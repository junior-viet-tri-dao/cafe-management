package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "equipment")//thietbi
public class EquipmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer id;

    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(max = 100, message = "Tên thiết bị không được vượt quá 100 ký tự")
    @Column(name = "equipment_name")
    private String equipmentName;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 0, message = "Số lượng phải lớn hơn hoặc bằng 0")
    @Column(name = "quantity")
    private Integer quantity;

    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    @Column(name = "notes")
    private String notes;

    @NotNull(message = "Ngày mua không được để trống")
    @PastOrPresent(message = "Ngày mua phải là ngày hiện tại hoặc trong quá khứ")
    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @NotNull(message = "Giá mua không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá mua không thể là số âm")
    @Column(name = "purchase_price")
    private Double purchasePrice;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    private List<ImportEntity> imports;
}
