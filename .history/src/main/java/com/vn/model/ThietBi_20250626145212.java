package com.vn.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ThietBi")
public class ThietBi {
    @Id
    @Column(name = "MaThietBi")
    @NotNull(message = "Mã thiết bị không được để trống")
    private Integer maThietBi;

    @Column(name = "TenThietBi", nullable = false, length = 100)
    private String tenThietBi;

    @Column(name = "NgayNhap", nullable = false)
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ngayNhap;


    @Column(name = "SoLuong", nullable = false)
    @NotNull(message = "Số lượng không được để trống")
    @Basic(optional = false)
    private Integer soLuong;

    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    @Column(name = "DonGiaMua", nullable = false)
    @NotNull(message = "Đơn giá mua không được để trống")
    private Double donGiaMua;
} 