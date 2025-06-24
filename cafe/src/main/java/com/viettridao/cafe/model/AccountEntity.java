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
@Entity
@Getter
@Setter
@Table(name = "accounts") // Tên bảng trong cơ sở dữ liệu: taikhoan
public class AccountEntity implements Serializable, UserDetails {
	// ID duy nhất của tài khoản, tự động tăng
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Integer id;

	// Tên đăng nhập của tài khoản, phải là duy nhất
	@Column(name = "username", unique = true)
	private String username;

	// Mật khẩu của tài khoản
	@Column(name = "password")
	private String password;

	// Quyền hạn của tài khoản (ví dụ: ROLE_ADMIN, ROLE_USER)
	@Column(name = "permissions")
	private String permission;

	// URL của ảnh đại diện của tài khoản
	@Column(name = "image_url")
	private String imageUrl;

	// Trạng thái xóa logic của tài khoản (true: đã xóa, false: chưa xóa)
	@Column(name = "is_deleted")
	private Boolean isDeleted;

	// Mối quan hệ một-một với thực thể EmployeeEntity
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private EmployeeEntity employee;

	// Mối quan hệ một-nhiều với danh sách chi phí (ExpenseEntity)
	@OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
	private List<ExpenseEntity> expenses;

	/**
	 * Lấy danh sách quyền hạn của tài khoản.
	 * 
	 * @return Collection chứa các quyền (GrantedAuthority) dựa trên permission
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Trả về danh sách quyền với một SimpleGrantedAuthority dựa trên trường
		// permission
		return List.of(new SimpleGrantedAuthority(this.permission));
	}

	/**
	 * Kiểm tra xem tài khoản có hết hạn hay không.
	 * 
	 * @return true nếu tài khoản chưa hết hạn
	 */
	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	/**
	 * Kiểm tra xem tài khoản có bị khóa hay không.
	 * 
	 * @return true nếu tài khoản không bị khóa
	 */
	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	/**
	 * Kiểm tra xem thông tin đăng nhập có hết hạn hay không.
	 * 
	 * @return true nếu thông tin đăng nhập chưa hết hạn
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	/**
	 * Kiểm tra xem tài khoản có được kích hoạt hay không.
	 * 
	 * @return true nếu tài khoản được kích hoạt
	 */
	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
}