package com.vn.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "chucVu")
    @ToString.Exclude
    private List<Users> users = new ArrayList<>();
} 