package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"ChucVu\"")
public class ChucVu {
    @Id
    @Column(name = "MaChucVu", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "Luong", nullable = false, precision = 18, scale = 2)
    private BigDecimal luong;

    @Size(max = 100)
    @NotNull
    @Nationalized
    @Column(name = "TenChucVu", nullable = false, length = 100)
    private String tenChucVu;

    @OneToMany(mappedBy = "maChucVu")
    private Set<NhanVien> nhanViens = new LinkedHashSet<>();

}