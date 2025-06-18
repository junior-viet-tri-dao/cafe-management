package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "DonXuat", schema = "dbo")
public class DonXuat {
    @Id
    @Column(name = "MaDonXuat", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaNhanVien", nullable = false)
    private NhanVien maNhanVien;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaHangHoa", nullable = false)
    private HangHoa maHangHoa;

    @NotNull
    @Column(name = "TongTienXuat", nullable = false, precision = 18, scale = 2)
    private BigDecimal tongTienXuat;

    @NotNull
    @Column(name = "NgayXuat", nullable = false)
    private LocalDate ngayXuat;

    @NotNull
    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

}