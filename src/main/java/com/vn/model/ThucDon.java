package com.vn.model;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ThucDon")
public class ThucDon {
    @Id
    @Column(name = "MaThucDon")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maThucDon;

    @Column(name = "TenMon", nullable = false, length = 100,columnDefinition = "nvarchar(100)")
    @NotBlank(message = "Tên món không được để trống")
    @Size(max = 100, message = "Tên món tối đa 100 ký tự")
    private String tenMon;

    @Column(name = "GiaTienHienTai", nullable = false)
    @NotNull(message = "Giá tiền hiện tại không được để trống")
    @DecimalMin(value = "0.01", message = "Giá tiền phải lớn hơn 0")
    private Double giaTienHienTai;

    @Column(name = "LoaiMon", nullable = false, length = 50 ,columnDefinition = "nvarchar(50)")
    @NotBlank(message = "Loại món không được để trống")
    @Size(max = 50, message = "Loại món tối đa 50 ký tự")
    private String loaiMon;

    @Column(name = "IsDeleted", nullable = false)
    @ColumnDefault("0")
    private Boolean isDeleted = false;
}