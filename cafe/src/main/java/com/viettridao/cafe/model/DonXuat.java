package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class DonXuat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDonXuat")
    private Integer maDonXuat;

    @Column(name = "TongTienXuat")
    private Double tongTienXuat;

    @Column(name = "NgayXuat")
    private LocalDate ngayXuat;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @ManyToOne
    @JoinColumn(name = "MaHangHoa")
    private HangHoa hangHoa;
}
