package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class KhuyenMai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKhuyenMai")
    private Integer maKhuyenMai;

    @Column(name = "TenKhuyenMai")
    private String tenKhuyenMai;

    @Column(name = "NgayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "NgayKetThuc")
    private LocalDate ngayKetThuc;

    @Column(name = "LoaiKhuyenMai")
    private String loaiKhuyenMai;

    @Column(name = "GiaTriGiam")
    private Double giaTriGiam;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "isDeleted")
    private Boolean isDeleted;
}
