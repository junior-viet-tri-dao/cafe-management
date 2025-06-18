package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ChiTietHoaDon", schema = "dbo")
public class ChiTietHoaDon {
    @EmbeddedId
    private ChiTietHoaDonId id;

    @MapsId("maThucDon")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaThucDon", nullable = false)
    private ThucDon maThucDon;

    @MapsId("maHoaDon")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaHoaDon", nullable = false)
    private HoaDon maHoaDon;

    @NotNull
    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @NotNull
    @Column(name = "GiaTaiThoiDiemBan", nullable = false, precision = 18, scale = 2)
    private BigDecimal giaTaiThoiDiemBan;

    @NotNull
    @Column(name = "ThanhTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal thanhTien;

}