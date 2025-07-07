package com.vn.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Ban")
public class Ban {
    @Id
    @Column(name = "MaBan")
    private Integer maBan;

    @Column(name = "TinhTrang", nullable = false, length = 50)
    private String tinhTrang;

    @Column(name = "TenBan", nullable = false, length = 50)
    private String tenBan;
} 