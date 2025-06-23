package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBan")
    private Integer maBan;

    @Column(name = "TinhTrang")
    private String tinhTrang;

    @Column(name = "TenBan")
    private String tenBan;
}
