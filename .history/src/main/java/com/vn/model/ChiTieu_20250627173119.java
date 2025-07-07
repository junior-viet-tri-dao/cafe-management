package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ChiTieu")
public class ChiTieu {
    @Id
    @Column(name = "MaChiTieu")
    private Integer maChiTieu;

    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien", nullable = false)
    private Users nhanVien;

    @Column(name = "SoTien", nullable = false)
    private Double soTien;

    @Column(name = "TenKhoanChi", length = 100,columnDefinition = "nvarchar(100)")
    private String tenKhoanChi;

    @Column(name = "NgayChi", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date ngayChi;
} 