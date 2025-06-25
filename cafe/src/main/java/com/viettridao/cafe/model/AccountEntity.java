package com.viettridao.cafe.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Lớp thực thể đại diện cho bảng tài khoản (accounts) trong cơ sở dữ liệu.
 * Triển khai UserDetails để hỗ trợ xác thực người dùng trong Spring Security.
 */
@Entity // Đánh dấu đây là một JPA Entity, ánh xạ với một bảng trong cơ sở dữ liệu
@Getter // Tự động tạo các phương thức getter cho tất cả các trường
@Setter // Tự động tạo các phương thức setter cho tất cả các trường
@Table(name = "accounts") // Chỉ định tên của bảng trong cơ sở dữ liệu là "accounts"
public class AccountEntity implements Serializable, UserDetails {
	// ID duy nhất của tài khoản, là khóa chính của bảng
	@Id
	// Cấu hình ID tự động tăng trong cơ sở dữ liệu
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// Ánh xạ thuộc tính này với cột "account_id" trong bảng
	@Column(name = "account_id")
	private Integer id;

	// Tên đăng nhập của tài khoản, phải là duy nhất trên toàn hệ thống
	@Column(name = "username", unique = true)
	private String username;

	// Mật khẩu của tài khoản (lưu ý: mật khẩu nên được mã hóa trước khi lưu vào DB)
	@Column(name = "password")
	private String password;

	// Quyền hạn của tài khoản (ví dụ: "ROLE_ADMIN", "ROLE_USER")
	@Column(name = "permissions")
	private String permission;

	// URL của ảnh đại diện của tài khoản (có thể là đường dẫn tương đối hoặc tuyệt
	// đối)
	@Column(name = "image_url")
	private String imageUrl;

	// Trạng thái xóa logic của tài khoản (true: đã được đánh dấu là xóa, false:
	// đang hoạt động)
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	/**
	 * Thiết lập mối quan hệ một-một (One-to-One) với thực thể `EmployeeEntity`.
	 * `mappedBy = "account"` chỉ ra rằng trường `account` trong `EmployeeEntity` là
	 * bên sở hữu mối quan hệ. `cascade = CascadeType.ALL` có nghĩa là mọi thao tác
	 * (thêm, sửa, xóa) trên AccountEntity sẽ ảnh hưởng đến EmployeeEntity liên
	 * quan.
	 */
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private EmployeeEntity employee;

	/**
	 * Thiết lập mối quan hệ một-nhiều (One-to-Many) với danh sách các khoản chi phí
	 * (`ExpenseEntity`). `mappedBy = "account"` chỉ ra rằng trường `account` trong
	 * `ExpenseEntity` là bên sở hữu mối quan hệ. `cascade = CascadeType.ALL` có
	 * nghĩa là mọi thao tác trên AccountEntity sẽ ảnh hưởng đến các ExpenseEntity
	 * liên quan.
	 */
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<ExpenseEntity> expenses;

	/**
	 * Lấy danh sách quyền hạn được cấp cho tài khoản này. Đây là một phần của giao
	 * diện UserDetails, quan trọng cho Spring Security.
	 *
	 * @return Collection chứa các đối tượng `GrantedAuthority` dựa trên trường
	 *         `permission`.
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Trả về một danh sách (Collection) chứa một quyền duy nhất được tạo từ chuỗi
		// permission
		return List.of(new SimpleGrantedAuthority(this.permission));
	}

	/**
	 * Kiểm tra xem tài khoản có còn hiệu lực (chưa hết hạn) hay không. Mặc định trả
	 * về true (không hết hạn) nếu không có logic kiểm tra cụ thể.
	 *
	 * @return true nếu tài khoản chưa hết hạn, ngược lại là false.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	/**
	 * Kiểm tra xem tài khoản có bị khóa hay không. Mặc định trả về true (không bị
	 * khóa) nếu không có logic kiểm tra cụ thể.
	 *
	 * @return true nếu tài khoản không bị khóa, ngược lại là false.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	/**
	 * Kiểm tra xem thông tin xác thực (mật khẩu) của tài khoản có hết hạn hay
	 * không. Mặc định trả về true (không hết hạn) nếu không có logic kiểm tra cụ
	 * thể.
	 *
	 * @return true nếu thông tin xác thực chưa hết hạn, ngược lại là false.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	/**
	 * Kiểm tra xem tài khoản có được kích hoạt (enable) hay không. Mặc định trả về
	 * true (được kích hoạt) nếu không có logic kiểm tra cụ thể.
	 *
	 * @return true nếu tài khoản được kích hoạt, ngược lại là false.
	 */
	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
}