package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "positions")//chucvu
public class PositionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Integer id;

    @NotNull(message = "Mức lương không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Mức lương không thể là số âm")
    @Column(name = "salary")
    private Double salary;

    @NotBlank(message = "Tên chức vụ không được để trống")
    @Size(max = 50, message = "Tên chức vụ không được vượt quá 50 ký tự")
    @Column(name = "position_name")
    private String positionName;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "position")
    private List<EmployeeEntity> employees;
}
