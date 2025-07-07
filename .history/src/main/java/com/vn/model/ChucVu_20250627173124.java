package com.vn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "ChucVu")
public class ChucVu {
    @Id
    @Column(name = "MaChucVu")
    private Integer maChucVu;

    @Column(name = "Luong", nullable = false)
    private Double luong;

    @Column(name = "TenChucVu", nullable = false, length = 100,columnDefinition = "nvarchar(50)")
    private String tenChucVu;
} 