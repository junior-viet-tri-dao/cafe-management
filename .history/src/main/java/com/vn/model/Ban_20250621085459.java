package com.vn.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Ban")
public class Ban {
    @Id
    @Column(name = "MaBan")
    @NotNull(message = "MaBan cannot be blank")
    private Integer maBan;

    @Column(name = "TinhTrang")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "TinhTrang cannot be blank")
    private TinhTrangBan tinhTrang;

    @Column(name = "TenBan", nullable = false, length = 50)
    @NotNull(message = "TenBan cannot be blank")
    private String tenBan;
} 