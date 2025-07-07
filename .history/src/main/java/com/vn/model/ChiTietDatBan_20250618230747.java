package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ChiTietDatBan")
@IdClass(ChiTietDatBan.ChiTietDatBanId.class)
public class ChiTietDatBan {
    @Id
    @ManyToOne
    @JoinColumn(name = "MaBan", referencedColumnName = "MaBan", nullable = false)
    private Ban ban;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaNhanVien", referencedColumnName = "MaNhanVien", nullable = false)
    private Users nhanVien;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaHoaDon", referencedColumnName = "MaHoaDon", nullable = false)
    private HoaDon hoaDon;

    @Column(name = "TenKhachHang", nullable = false, length = 100)
    private String tenKhachHang;

    @Column(name = "SdtKhachHang", length = 15)
    private String sdtKhachHang;

    @Column(name = "NgayGioDat", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayGioDat;

    @Data
    public static class ChiTietDatBanId implements Serializable {
        private Integer ban;
        private Integer nhanVien;
        private Integer hoaDon;
    }
} 