package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class DonNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDonNhap")
    private Integer maDonNhap;

    @Column(name = "TongTien")
    private Double tongTien;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "NgayNhap")
    private LocalDate ngayNhap;

    @ManyToOne
    @JoinColumn(name = "MaHangHoa")
    private HangHoa hangHoa;
}
