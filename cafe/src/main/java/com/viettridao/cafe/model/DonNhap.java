package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "DonNhap", schema = "dbo")
public class DonNhap {
    @Id
    @Size(max = 10)
    @Column(name = "MaDonNhap", nullable = false, length = 10)
    private String maDonNhap;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaNhanVien", nullable = false)
    private NhanVien maNhanVien;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaThietBi", nullable = false)
    private ThietBi maThietBi;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaHangHoa", nullable = false)
    private HangHoa maHangHoa;

    @NotNull
    @Column(name = "NgayNhap", nullable = false)
    private LocalDate ngayNhap;

    @NotNull
    @Column(name = "TongTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTien;

    @NotNull
    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

}