package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ThietBi")
public class ThietBi {
    @Id
    @Column(name = "MaThietBi")
    private Integer maThietBi;

    @Column(name = "TenThietBi", nullable = false, length = 100)
    private String tenThietBi;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "GhiChu", length = 255)
    private String ghiChu;

    // @Column(name = "NgayMua", nullable = false)
    // @Temporal(TemporalType.DATE)
    // private Date ngayMua;

    @Column(name = "DonGiaMua", nullable = false)
    private Double donGiaMua;
} 