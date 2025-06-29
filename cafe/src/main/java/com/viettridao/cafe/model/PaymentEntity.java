package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "customer_paid")
    private Long customerPaid;

    @Column(name = "change_amount")
    private Long changeAmount;

    @Column(name = "note")
    private String note;
}
