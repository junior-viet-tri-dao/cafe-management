package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class ChiTieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTieu")
    private Integer maChiTieu;

    @Column(name = "SoTien")
    private Double soTien;

    @Column(name = "TenKhoanChi")
    private String tenKhoanChi;

    @Column(name = "NgayChi")
    private LocalDate ngayChi;
}
