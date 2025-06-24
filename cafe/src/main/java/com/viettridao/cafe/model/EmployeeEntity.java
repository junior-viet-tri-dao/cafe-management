package com.viettridao.cafe.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Lớp thực thể đại diện cho bảng nhân viên (employees) trong cơ sở dữ liệu.
 * Chứa thông tin cá nhân và các mối quan hệ với các thực thể khác như tài
 * khoản, vị trí và danh sách liên quan.
 */
@Entity
@Getter
@Setter
@Table(name = "employees") // Tên bảng trong cơ sở dữ liệu: nhanvien
public class EmployeeEntity {
	// ID duy nhất của nhân viên, tự động tăng
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private Integer id;

	// Họ và tên đầy đủ của nhân viên, giới hạn 50 ký tự
	@Column(name = "full_name", length = 50)
	private String fullName;

	// Số điện thoại của nhân viên, giới hạn 20 ký tự
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	// Địa chỉ của nhân viên, giới hạn 100 ký tự
	@Column(name = "address", length = 100)
	private String address;

	// Trạng thái xóa logic của nhân viên (true: đã xóa, false: chưa xóa)
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	// Mối quan hệ một-một với thực thể AccountEntity, liên kết qua account_id
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id")
	private AccountEntity account;

	// Mối quan hệ nhiều-một với thực thể PositionEntity, liên kết qua position_id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "position_id")
	private PositionEntity position;

	// Mối quan hệ một-nhiều với danh sách nhập hàng (ImportEntity)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<ImportEntity> imports;

	// Mối quan hệ một-nhiều với danh sách xuất hàng (ExportEntity)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<ExportEntity> exports;

	// Mối quan hệ một-nhiều với danh sách đặt bàn (ReservationEntity)
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<ReservationEntity> reservations;
}