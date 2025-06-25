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
 * Chứa thông tin cá nhân và các mối quan hệ với các thực thể khác như tài khoản,
 * vị trí và danh sách các hoạt động liên quan.
 */
@Entity // Đánh dấu đây là một JPA Entity, ánh xạ với một bảng trong cơ sở dữ liệu
@Getter // Tự động tạo các phương thức getter cho tất cả các trường
@Setter // Tự động tạo các phương thức setter cho tất cả các trường
@Table(name = "employees") // Chỉ định tên của bảng trong cơ sở dữ liệu là "employees"
public class EmployeeEntity {
	// ID duy nhất của nhân viên, là khóa chính của bảng
	@Id
	// Cấu hình ID tự động tăng trong cơ sở dữ liệu
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Ánh xạ thuộc tính này với cột "employee_id" trong bảng
	@Column(name = "employee_id")
	private Integer id;

	// Họ và tên đầy đủ của nhân viên, giới hạn tối đa 50 ký tự
	@Column(name = "full_name", length = 50)
	private String fullName;

	// Số điện thoại liên hệ của nhân viên, giới hạn tối đa 20 ký tự
	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	// Địa chỉ hiện tại của nhân viên, giới hạn tối đa 100 ký tự
	@Column(name = "address", length = 100)
	private String address;

	// Trạng thái xóa logic của nhân viên (true: đã được đánh dấu là xóa, false: đang hoạt động)
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	/**
	 * Thiết lập mối quan hệ một-một (One-to-One) với thực thể `AccountEntity`.
	 * `cascade = CascadeType.ALL` nghĩa là mọi thao tác (thêm, sửa, xóa) trên EmployeeEntity
	 * sẽ ảnh hưởng đến AccountEntity liên quan.
	 * `JoinColumn(name = "account_id")` chỉ định cột `account_id` trong bảng `employees`
	 * là khóa ngoại liên kết đến bảng `accounts`.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "account_id")
	private AccountEntity account;

	/**
	 * Thiết lập mối quan hệ nhiều-một (Many-to-One) với thực thể `PositionEntity`.
	 * `cascade = CascadeType.ALL` nghĩa là mọi thao tác trên EmployeeEntity
	 * sẽ ảnh hưởng đến PositionEntity liên quan (cẩn trọng khi sử dụng `ALL` trên ManyToOne).
	 * `JoinColumn(name = "position_id")` chỉ định cột `position_id` trong bảng `employees`
	 * là khóa ngoại liên kết đến bảng `positions`.
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "position_id")
	private PositionEntity position;

	/**
	 * Thiết lập mối quan hệ một-nhiều (One-to-Many) với danh sách các bản ghi nhập hàng (`ImportEntity`).
	 * `mappedBy = "employee"` chỉ ra rằng trường `employee` trong `ImportEntity` là bên sở hữu mối quan hệ.
	 * `cascade = CascadeType.ALL` nghĩa là mọi thao tác trên EmployeeEntity sẽ ảnh hưởng đến các ImportEntity liên quan.
	 */
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<ImportEntity> imports;

	/**
	 * Thiết lập mối quan hệ một-nhiều (One-to-Many) với danh sách các bản ghi xuất hàng (`ExportEntity`).
	 * `mappedBy = "employee"` chỉ ra rằng trường `employee` trong `ExportEntity` là bên sở hữu mối quan hệ.
	 * `cascade = CascadeType.ALL` nghĩa là mọi thao tác trên EmployeeEntity sẽ ảnh hưởng đến các ExportEntity liên quan.
	 */
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<ExportEntity> exports;

	/**
	 * Thiết lập mối quan hệ một-nhiều (One-to-Many) với danh sách các bản ghi đặt bàn (`ReservationEntity`).
	 * `mappedBy = "employee"` chỉ ra rằng trường `employee` trong `ReservationEntity` là bên sở hữu mối quan hệ.
	 * `cascade = CascadeType.ALL` nghĩa là mọi thao tác trên EmployeeEntity sẽ ảnh hưởng đến các ReservationEntity liên quan.
	 */
	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
	private List<ReservationEntity> reservations;
}