package com.vn.model;

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

    @Column(name = "TenDonVi", nullable = false, length = 50)
    private String tenDonVi;
} 