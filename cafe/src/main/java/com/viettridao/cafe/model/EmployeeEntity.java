package com.viettridao.cafe.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "employees")//nhanvien
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Integer id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

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
