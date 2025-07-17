package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

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

    @Column(name = "TenKhachHang", nullable = false, length = 100,columnDefinition = "nvarchar(100)")
    private String tenKhachHang;

    @Column(name = "SdtKhachHang", length = 15)
    private String sdtKhachHang;

    @Column(name = "NgayGioDat", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date ngayGioDat;

    @Data
    public static class ChiTietDatBanId implements Serializable {
        private Integer ban;
        private Integer nhanVien;
        private Integer hoaDon;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChiTietDatBanId that = (ChiTietDatBanId) o;
            return Objects.equals(ban, that.ban) && 
                   Objects.equals(nhanVien, that.nhanVien) && 
                   Objects.equals(hoaDon, that.hoaDon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ban, nhanVien, hoaDon);
        }
    }
} 