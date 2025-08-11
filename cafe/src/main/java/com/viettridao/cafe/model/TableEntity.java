package com.viettridao.cafe.model;

import com.viettridao.cafe.common.TableStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tables")//ban
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Integer id;

    @NotNull(message = "Trạng thái bàn không được để trống")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TableStatus status;

    @NotBlank(message = "Tên bàn không được để trống")
    @Size(max = 20, message = "Tên bàn không được vượt quá 20 ký tự")
    @Column(name = "table_name")
    private String tableName;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations;
}
