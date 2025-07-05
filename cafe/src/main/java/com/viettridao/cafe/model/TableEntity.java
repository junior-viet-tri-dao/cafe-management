package com.viettridao.cafe.model;

import java.util.List;

import com.viettridao.cafe.common.TableStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tables") // ban
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

	@OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
	private List<ReservationEntity> reservations;
}
