package com.viettridao.cafe.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Lớp thực thể đại diện cho bảng thiết bị (equipment) trong cơ sở dữ liệu. Chứa
 * thông tin về thiết bị và mối quan hệ với các bản ghi nhập hàng.
 */
@Getter
@Setter
@Entity
@Table(name = "equipment") // Tên bảng trong cơ sở dữ liệu: thietbi
public class EquipmentEntity {
	// ID duy nhất của thiết bị, tự động tăng
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "equipment_id")
	private Integer id;

	// Tên của thiết bị, giới hạn 50 ký tự, không được để trống
	@Column(name = "equipment_name", length = 50, nullable = false)
	private String equipmentName;

	// Số lượng thiết bị
	@Column(name = "quantity")
	private Integer quantity;

	// Ghi chú về thiết bị, giới hạn 255 ký tự
	@Column(name = "notes", length = 255)
	private String notes;

	// Ngày mua thiết bị
	@Column(name = "purchase_date")
	private LocalDate purchaseDate;

	// Giá mua thiết bị
	@Column(name = "purchase_price")
	private Double purchasePrice;

	// Trạng thái xóa logic của thiết bị (true: đã xóa, false: chưa xóa)
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	// Mối quan hệ một-nhiều với danh sách nhập hàng (ImportEntity)
	@OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
	private List<ImportEntity> imports;
}