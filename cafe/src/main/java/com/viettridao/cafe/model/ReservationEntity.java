package com.viettridao.cafe.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "table_reservations_detail") // chitietdatban
public class ReservationEntity {
	@EmbeddedId
	private ReservationKey id;

	@ManyToOne
	@MapsId("idTable")
	@JoinColumn(name = "table_id")
	private TableEntity table;

	@ManyToOne
	@MapsId("idEmployee")
	@JoinColumn(name = "employee_id")
	private EmployeeEntity employee;

	@ManyToOne
	@MapsId("idInvoice")
	@JoinColumn(name = "invoice_id")
	private InvoiceEntity invoice;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "reservation_time")
	private LocalTime reservationTime;

	@Column(name = "customer_phone_number")
	private String customerPhone;

	@Column(name = "reservation_datetime")
	private LocalDate reservationDate;

	@Column(name = "is_deleted")
	private Boolean isDeleted;
}
