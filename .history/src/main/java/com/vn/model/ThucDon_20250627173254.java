package com.vn.model;

import jakarta.persistence.*;
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
    private String tenMon;

    @Column(name = "GiaTienHienTai", nullable = false)
    private Double giaTienHienTai;

    @Column(name = "LoaiMon", nullable = false, length = 50)
    private String loaiMon;

    @Column(name = "IsDeleted", nullable = false)
    private Boolean isDeleted = false;
} 