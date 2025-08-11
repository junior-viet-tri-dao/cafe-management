package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "table_reservations_detail")//chitietdatban
public class ReservationEntity {
    @EmbeddedId
    private ReservationKey id;

    @NotNull(message = "Bàn không được để trống")
    @ManyToOne
    @MapsId("idTable")
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @NotNull(message = "Nhân viên không được để trống")
    @ManyToOne
    @MapsId("idEmployee")
    @JoinColumn(name = "employee_id")
    private EmployeeEntity employee;

    @NotNull(message = "Hóa đơn không được để trống")
    @ManyToOne
    @MapsId("idInvoice")
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;


    @Size(max = 100, message = "Tên khách hàng không được vượt quá 100 ký tự")
    @Column(name = "customer_name")
    private String customerName;


    @Pattern(regexp = "^[0-9]{10,11}$", message = "Số điện thoại không hợp lệ (10 hoặc 11 số)")
    @Column(name = "customer_phone_number")
    private String customerPhone;


//    @FutureOrPresent(message = "Thời gian đặt bàn phải là trong tương lai hoặc hiện tại")
    @Column(name = "reservation_datetime")
    private LocalDateTime reservationDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
