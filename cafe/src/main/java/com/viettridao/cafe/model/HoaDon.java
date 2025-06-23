package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaHoaDon")
    private Integer maHoaDon;

    @Column(name = "NgayGioTao")
    private LocalDateTime ngayGioTao;

    @Column(name = "TongTien")
    private Double tongTien;

    @Column(name = "TrangThai")
    private Boolean trangThai;
}
