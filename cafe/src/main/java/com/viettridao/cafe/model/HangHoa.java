package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class HangHoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHangHoa")
    private Integer maHangHoa;

    @Column(name = "TenHangHoa")
    private String tenHangHoa;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGia")
    private Double donGia;

    @OneToMany(mappedBy = "hangHoa")
    private List<DonNhap> listDonNhap;

    @OneToMany(mappedBy = "hangHoa")
    private List<DonXuat> listDonXuat;

    @ManyToOne
    @JoinColumn(name = "MaDonVi")
    private DonViTinh donVi;

    @ManyToMany
    @JoinTable(
            name = "ChiTietThucDon",
            joinColumns = @JoinColumn(name = "MaHangHoa"),
            inverseJoinColumns = @JoinColumn(name = "MaThucDon")
    )
    private List<ThucDon> listThucDon;
}
