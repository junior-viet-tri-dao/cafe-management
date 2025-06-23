package com.viettridao.cafe.model;

import com.viettridao.cafe.common.TableStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tables")//ban
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TableStatus status;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
