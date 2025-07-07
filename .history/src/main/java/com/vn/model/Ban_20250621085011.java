package com.vn.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Ban")
public class Ban {
    @Id
    @Column(name = "MaBan")
    @NotBlank(message = "MaBan cannot be blank")
    private Integer maBan;

    @Column(name = "TinhTrang")
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "TinhTrang cannot be blank")
    private TinhTrangBan tinhTrang;

    @Column(name = "TenBan", nullable = false, length = 50)
    @NotBlank(message = "TenBan cannot be blank")
    private String tenBan;
} 