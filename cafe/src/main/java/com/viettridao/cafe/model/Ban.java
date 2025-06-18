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
@Table(name = "Ban", schema = "dbo")
public class Ban {
    @Id
    @Column(name = "MaBan", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "TinhTrang", nullable = false, length = 50)
    private String tinhTrang;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "TenBan", nullable = false, length = 50)
    private String tenBan;

    @OneToMany(mappedBy = "maBan")
    private Set<ChiTietDatBan> chiTietDatBans = new LinkedHashSet<>();

}