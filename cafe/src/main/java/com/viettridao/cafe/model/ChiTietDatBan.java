package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "ChiTietDatBan", schema = "dbo")
public class ChiTietDatBan {
    @EmbeddedId
    private ChiTietDatBanId id;

    @MapsId("maBan")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaBan", nullable = false)
    private Ban maBan;

    @MapsId("maNhanVien")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaNhanVien", nullable = false)
    private NhanVien maNhanVien;

    @MapsId("maHoaDon")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaHoaDon", nullable = false)
    private HoaDon maHoaDon;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "TenKhachHang", nullable = false, length = 100)
    private String tenKhachHang;

    @Size(max = 15)
    @Nationalized
    @Column(name = "SdtKhachHang", length = 15)
    private String sdtKhachHang;

    @NotNull
    @Column(name = "NgayGioDat", nullable = false)
    private Instant ngayGioDat;

}