package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "table_reservations_detail")//chitietdatban
public class ReservationEntity {
    @EmbeddedId
    private ReservationKey id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone_number")
    private String customerPhone;

    @Column(name = "reservation_datetime")
    private LocalDate reservationDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
