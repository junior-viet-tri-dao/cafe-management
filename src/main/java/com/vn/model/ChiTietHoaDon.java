package com.vn.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ChiTietHoaDon")
@IdClass(ChiTietHoaDon.ChiTietHoaDonId.class)
public class ChiTietHoaDon {

    @Id
    @ManyToOne
    @JoinColumn(name = "MaThucDon", referencedColumnName = "MaThucDon", nullable = false)
    private ThucDon thucDon;

    @Id
    @ManyToOne
    @JoinColumn(name = "MaHoaDon", referencedColumnName = "MaHoaDon", nullable = false)
    private HoaDon hoaDon;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "GiaTaiThoiDiemBan", nullable = false)
    private Double giaTaiThoiDiemBan;

    @Column(name = "ThanhTien", nullable = false)
    private Double thanhTien;

    @Data
    public static class ChiTietHoaDonId implements Serializable {
        private Integer thucDon;
        private Integer hoaDon;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ChiTietHoaDonId that = (ChiTietHoaDonId) o;
            return Objects.equals(thucDon, that.thucDon) && Objects.equals(hoaDon, that.hoaDon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(thucDon, hoaDon);
        }
    }
} 