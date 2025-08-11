package com.viettridao.cafe.model;

import com.viettridao.cafe.common.InvoiceStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Tổng tiền không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tổng tiền không thể là số âm")
    @Column(name = "total_amount")
    private Double totalAmount;

    @NotNull(message = "Ngày tạo hóa đơn không được để trống")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull(message = "Trạng thái hóa đơn không được để trống")
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
