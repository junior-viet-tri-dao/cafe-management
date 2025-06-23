package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ChucVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChucVu")
    private Integer maChucVu;

    @Column(name = "Luong")
    private Double luong;

    @Column(name = "TenChucVu")
    private String tenChucVu;

    @OneToMany(mappedBy = "chucVu", fetch = FetchType.EAGER)
    private List<NhanVien> nhanVienList;
}
