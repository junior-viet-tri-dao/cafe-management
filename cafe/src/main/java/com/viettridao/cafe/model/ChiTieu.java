package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "ChiTieu", schema = "dbo")
public class ChiTieu {
    @Id
    @Column(name = "MaChiTieu", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaTaiKhoan", nullable = false)
    private TaiKhoan maTaiKhoan;

    @NotNull
    @Column(name = "SoTien", nullable = false, precision = 18, scale = 2)
    private BigDecimal soTien;

    @Size(max = 100)
    @Nationalized
    @Column(name = "TenKhoanChi", length = 100)
    private String tenKhoanChi;

    @NotNull
    @Column(name = "NgayChi", nullable = false)
    private LocalDate ngayChi;

}