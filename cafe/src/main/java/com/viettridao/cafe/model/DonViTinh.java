package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class DonViTinh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDonViTinh")
    private Integer maDonViTinh;

    @Column(name = "TenDonVi")
    private String tenDonVi;

    @OneToMany(mappedBy = "donVi")
    private List<HangHoa> listHangHoa;

    @OneToMany(mappedBy = "donViTinh")
    private List<ThanhPhanThucDon> listThanhPhanThucDon;
}
