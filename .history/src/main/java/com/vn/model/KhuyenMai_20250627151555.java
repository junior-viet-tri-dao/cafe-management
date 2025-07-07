package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "KhuyenMai")
public class KhuyenMai {
    @Id
    @Column(name = "MaKhuyenMai")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maKhuyenMai;

    @Column(name = "TenKhuyenMai", nullable = false, length = 100)
    private String tenKhuyenMai;

    @Column(name = "NgayBatDau", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayBatDau;

    @Column(name = "NgayKetThuc", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayKetThuc;

    @Column(name = "LoaiKhuyenMai", nullable = false, length = 50)
    private String loaiKhuyenMai;

    @Column(name = "GiaTriGiam", nullable = false)
    private Double giaTriGiam;

} 