package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "HoaDon", schema = "dbo")
public class HoaDon {
    @Id
    @Column(name = "MaHoaDon", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "TongTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    @NotNull
    @Column(name = "NgayGioTao", nullable = false)
    private Instant ngayGioTao;

    @NotNull
    @Column(name = "TrangThai", nullable = false)
    private Boolean trangThai = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaKhuyenMai")
    private KhuyenMai maKhuyenMai;

    @OneToMany(mappedBy = "maHoaDon")
    private Set<ChiTietDatBan> chiTietDatBans = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maHoaDon")
    private Set<ChiTietHoaDon> chiTietHoaDons = new LinkedHashSet<>();

}