package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ThucDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaThucDon")
    private Integer maThucDon;

    @Column(name = "TenMon")
    private String tenMon;

    @Column(name = "GiaTienHienTai")
    private Double giaTienHienTai;

    @Column(name = "LoaiMon")
    private String loaiMon;

    @Column(name = "isDeleted")
    private Boolean isDeleted;

    @ManyToMany(mappedBy = "listThucDon")
    private List<HangHoa> listHangHoa;

    @OneToMany(mappedBy = "thucDon", cascade = CascadeType.ALL)
    private List<ThanhPhanThucDon> listThanhPhanThucDon;
}
