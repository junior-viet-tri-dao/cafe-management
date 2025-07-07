package com.vn.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import com.vn.validator.DateAfter;

@Entity
@Getter
@Setter
@ToString
@Table(name = "KhuyenMai")
@DateAfter(startDateField = "ngayBatDau", endDateField = "ngayKetThuc", message = "Ngày kết thúc phải sau ngày bắt đầu")
public class KhuyenMai {
    @Id
    @Column(name = "MaKhuyenMai")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maKhuyenMai;

    @Column(name = "TenKhuyenMai", nullable = false, length = 100, columnDefinition = "nvarchar(100)")
    @NotBlank(message = "Tên khuyến mãi không được để trống")
    @Size(max = 100, message = "Tên khuyến mãi không được vượt quá 100 ký tự")
    private String tenKhuyenMai;

    @Column(name = "NgayBatDau", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Ngày bắt đầu không được để trống")
    private Date ngayBatDau;

    @Column(name = "NgayKetThuc", nullable = false)
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Ngày kết thúc không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayKetThuc;

    @Column(name = "LoaiKhuyenMai", nullable = false, length = 50, columnDefinition = "nvarchar(50)")
    @NotBlank(message = "Loại khuyến mãi không được để trống")
    @Size(max = 50, message = "Loại khuyến mãi không được vượt quá 50 ký tự")
    private String loaiKhuyenMai;

    @Column(name = "GiaTriGiam", nullable = false)
    @NotNull(message = "Giá trị giảm không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá trị giảm phải lớn hơn 0")
    private Double giaTriGiam;

    @Column(name = "IsDeleted", nullable = false)
    @ColumnDefault("0")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "khuyenMai")
    private List<HoaDon> hoaDons = new ArrayList<>();
}