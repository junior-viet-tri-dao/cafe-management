package com.viettridao.cafe.model;

import java.time.LocalDateTime;
import java.util.List;

import com.viettridao.cafe.common.InvoiceStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoices")//hoadon
public class InvoiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer id;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvoiceStatus status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "promotion_id")
    private PromotionEntity promotion;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceDetailEntity> invoiceDetails;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<ReservationEntity> reservations;
}
