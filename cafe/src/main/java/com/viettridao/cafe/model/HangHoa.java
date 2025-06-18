package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "HangHoa", schema = "dbo")
public class HangHoa {
    @Id
    @Column(name = "MaHangHoa", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "TenHangHoa", nullable = false, length = 100)
    private String tenHangHoa;

    @NotNull
    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaDonViTinh", nullable = false)
    private DonViTinh maDonViTinh;

    @NotNull
    @Column(name = "DonGia", nullable = false, precision = 18, scale = 2)
    private BigDecimal donGia;

    @OneToMany(mappedBy = "maHangHoa")
    private Set<ChiTietThucDon> chiTietThucDons = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maHangHoa")
    private Set<DonNhap> donNhaps = new LinkedHashSet<>();

    @OneToMany(mappedBy = "maHangHoa")
    private Set<DonXuat> donXuats = new LinkedHashSet<>();

}