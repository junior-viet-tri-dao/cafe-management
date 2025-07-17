package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import groovy.transform.builder.Builder;

@Getter
@Setter
@Entity
@Table(name = "equipment") // thietbi
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Integer id;

    @Column(name = "equipment_name")
    private String equipmentName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_price", precision = 15)
    private BigDecimal purchasePrice;

    @Column(name = "total_amount", precision = 15)
    private BigDecimal totalAmount;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
