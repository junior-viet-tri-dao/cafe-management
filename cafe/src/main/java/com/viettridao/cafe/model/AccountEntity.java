package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "permissions")
    private String permission;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private EmployeeEntity employee;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<ExpenseEntity> expenses;
}
