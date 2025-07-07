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
    @Column(name = "MaBan", unique = true)
    @NotNull(message = "Mã Bàn Không được để trống")
    private Integer maBan;

    @Column(name = "TinhTrang")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Tình Trạng Không được để trống")
    private TinhTrangBan tinhTrang;

    @Column(name = "TenBan", nullable = false, length = 50,columnDefinition = "nvarchar(50)")
    @NotBlank(message = "Tên Bàn Không được để trống")
    private String tenBan;
} 