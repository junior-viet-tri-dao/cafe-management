package com.vn.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "DonViTinh")
public class DonViTinh {
    @Id
    @Column(name = "MaDonViTinh")
    private Integer maDonViTinh;

    @Column(name = "TenDonVi", nullable = false, length = 50,columnDefinition = "nvarchar(50)")
    private String tenDonVi;

    @OneToMany(mappedBy = "donViTinh", cascade = CascadeType.ALL)
    private List<HangHoa> hanghoas;
} 