package com.viettridao.cafe.model;

import com.viettridao.cafe.common.InvoiceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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
