package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "units")//donvitinh
public class UnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Integer id;

    @NotBlank(message = "Tên đơn vị tính không được để trống")
    @Size(max = 50, message = "Tên đơn vị tính không được vượt quá 50 ký tự")
    @Column(name = "unit_name")
    private String unitName;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
    private List<ProductEntity> products;
}
