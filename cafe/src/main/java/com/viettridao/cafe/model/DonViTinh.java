package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "DonViTinh", schema = "dbo")
public class DonViTinh {
    @Id
    @Column(name = "MaDonViTinh", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "TenDonVi", nullable = false, length = 50)
    private String tenDonVi;

    @OneToMany(mappedBy = "maDonViTinh")
    private Set<HangHoa> hangHoas = new LinkedHashSet<>();

}