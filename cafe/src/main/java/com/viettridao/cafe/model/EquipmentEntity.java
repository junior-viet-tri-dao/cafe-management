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
@Getter // Tự động tạo các phương thức getter cho tất cả các trường
@Setter // Tự động tạo các phương thức setter cho tất cả các trường
@Entity // Đánh dấu đây là một JPA Entity, ánh xạ với một bảng trong cơ sở dữ liệu
@Table(name = "equipment") // Chỉ định tên của bảng trong cơ sở dữ liệu là "equipment"
public class EquipmentEntity {
	// ID duy nhất của thiết bị, là khóa chính của bảng
	@Id
	// Cấu hình ID tự động tăng trong cơ sở dữ liệu
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Ánh xạ thuộc tính này với cột "equipment_id" trong bảng
	@Column(name = "equipment_id")
	private Integer id;

	// Tên của thiết bị, giới hạn 50 ký tự và không được để trống (nullable = false)
	@Column(name = "equipment_name", length = 50, nullable = false)
	private String equipmentName;

	// Số lượng thiết bị hiện có
	@Column(name = "quantity")
	private Integer quantity;

	// Ghi chú bổ sung về thiết bị, giới hạn 255 ký tự
	@Column(name = "notes", length = 255)
	private String notes;

	// Ngày mua thiết bị
	@Column(name = "purchase_date")
	private LocalDate purchaseDate;

	// Giá mua của thiết bị tại thời điểm mua
	@Column(name = "purchase_price")
	private Double purchasePrice;

	// Trạng thái xóa logic của thiết bị (true: đã được đánh dấu là xóa, false: đang
	// hoạt động)
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	/**
	 * Thiết lập mối quan hệ một-nhiều (One-to-Many) với danh sách các bản ghi nhập
	 * hàng (`ImportEntity`). `mappedBy = "equipment"` chỉ ra rằng trường
	 * `equipment` trong `ImportEntity` là bên sở hữu mối quan hệ. `cascade =
	 * CascadeType.ALL` nghĩa là mọi thao tác (thêm, sửa, xóa) trên EquipmentEntity
	 * sẽ ảnh hưởng đến các ImportEntity liên quan.
	 */
	@OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
	private List<ImportEntity> imports;
}