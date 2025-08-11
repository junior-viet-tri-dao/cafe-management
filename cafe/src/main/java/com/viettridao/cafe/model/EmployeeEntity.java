package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "employees")//nhanvien
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer id;

    @NotBlank(message = "Họ và tên không được để trống")
    @Size(max = 100, message = "Họ và tên không được vượt quá 100 ký tự")
    @Column(name = "full_name")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ (10 hoặc 11 số)")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    @Column(name = "address")
    private String address;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @NotNull(message = "Tài khoản không được để trống")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @NotNull(message = "Chức vụ không được để trống")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id")
    private PositionEntity position;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<ImportEntity> imports;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<ExportEntity> exports;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations;
}
