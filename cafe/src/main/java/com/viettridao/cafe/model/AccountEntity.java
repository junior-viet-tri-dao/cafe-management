package com.viettridao.cafe.model; // Khai báo gói cho lớp AccountEntity

import jakarta.persistence.*; // Nhập tất cả các chú thích từ gói jakarta.persistence (ví dụ: Entity, Id, GeneratedValue, Column, Table, OneToOne, OneToMany, CascadeType)
import lombok.Getter; // Nhập chú thích Getter của Lombok để tự động tạo phương thức getter
import lombok.Setter; // Nhập chú thích Setter của Lombok để tự động tạo phương thức setter
import org.springframework.security.core.GrantedAuthority; // Nhập giao diện GrantedAuthority từ Spring Security
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Nhập lớp SimpleGrantedAuthority từ Spring Security
import org.springframework.security.core.userdetails.UserDetails; // Nhập giao diện UserDetails từ Spring Security

import java.io.Serializable; // Nhập giao diện Serializable để đối tượng có thể được tuần tự hóa
import java.util.Collection; // Nhập giao diện Collection từ gói java.util
import java.util.List; // Nhập lớp List từ gói java.util

/**
 * AccountEntity
 *
 * Lớp này đại diện cho một thực thể tài khoản trong cơ sở dữ liệu.
 * Nó cũng triển khai giao diện UserDetails của Spring Security để hỗ trợ xác thực.
 *
 * Phiên bản 1.0
 *
 * Ngày: 2025-07-23
 *
 * Bản quyền (c) 2025 VietTriDao. Đã đăng ký bản quyền.
 *
 * Nhật ký sửa đổi:
 * NGÀY                 TÁC GIẢ          MÔ TẢ
 * -----------------------------------------------------------------------
 * 2025-07-23           Hoa Mãn Lâu     Tạo và định dạng ban đầu.
 */
@Entity // Đánh dấu lớp này là một thực thể JPA
@Getter // Tự động tạo tất cả các phương thức getter cho các trường của lớp
@Setter // Tự động tạo tất cả các phương thức setter cho các trường của lớp
@Table(name = "accounts") // Ánh xạ thực thể này tới bảng "accounts" trong cơ sở dữ liệu
public class AccountEntity implements Serializable, UserDetails { // Triển khai Serializable (để có thể tuần tự hóa) và UserDetails (để tích hợp với Spring Security)

    @Id // Đánh dấu trường này là khóa chính của thực thể
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Cấu hình chiến lược tạo giá trị cho khóa chính là tự động tăng (IDENTITY)
    @Column(name = "account_id") // Ánh xạ trường này tới cột "account_id" trong bảng cơ sở dữ liệu
    private Integer id; // ID duy nhất của tài khoản

    @Column(name = "username", unique = true) // Ánh xạ trường này tới cột "username" và đảm bảo giá trị là duy nhất trong bảng
    private String username; // Tên đăng nhập của tài khoản

    @Column(name = "password") // Ánh xạ trường này tới cột "password"
    private String password; // Mật khẩu của tài khoản (thường được mã hóa)

    @Column(name = "permissions") // Ánh xạ trường này tới cột "permissions"
    private String permission; // Quyền hạn của tài khoản (ví dụ: "ADMIN", "USER", "STAFF")

    @Column(name = "image_url") // Ánh xạ trường này tới cột "image_url"
    private String imageUrl; // URL hình ảnh đại diện của tài khoản

    @Column(name = "is_deleted") // Ánh xạ trường này tới cột "is_deleted"
    private Boolean isDeleted; // Cờ boolean chỉ ra liệu tài khoản có bị xóa mềm hay không

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL) // Định nghĩa mối quan hệ một-một với EmployeeEntity, được ánh xạ bởi trường "account" trong EmployeeEntity. CascadeType.ALL nghĩa là mọi thao tác (persist, merge, remove, refresh, detach) trên AccountEntity sẽ được áp dụng cho EmployeeEntity liên quan.
    private EmployeeEntity employee; // Đối tượng nhân viên liên kết với tài khoản này

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL) // Định nghĩa mối quan hệ một-nhiều với ExpenseEntity, được ánh xạ bởi trường "account" trong ExpenseEntity. CascadeType.ALL nghĩa là mọi thao tác trên AccountEntity sẽ được áp dụng cho các ExpenseEntity liên quan.
    private List<ExpenseEntity> expenses; // Danh sách các chi phí liên quan đến tài khoản này

    /**
     * Trả về danh sách các quyền hạn được cấp cho người dùng.
     * Phương thức này là một phần của giao diện UserDetails, được Spring Security sử dụng.
     *
     * @return Một Collection các GrantedAuthority.
     */
    @Override // Ghi đè phương thức từ giao diện UserDetails
    public Collection<? extends GrantedAuthority> getAuthorities() { // Định nghĩa phương thức để lấy quyền hạn của người dùng
        return List.of(new SimpleGrantedAuthority(this.permission)); // Trả về một danh sách chứa quyền hạn của người dùng (chuyển đổi từ chuỗi permission sang SimpleGrantedAuthority)
    }

    /**
     * Cho biết liệu tài khoản của người dùng có hết hạn hay không.
     * Phương thức này là một phần của giao diện UserDetails, được Spring Security sử dụng.
     *
     * @return true nếu tài khoản không hết hạn, ngược lại là false.
     */
    @Override // Ghi đè phương thức từ giao diện UserDetails
    public boolean isAccountNonExpired() { // Định nghĩa phương thức kiểm tra tài khoản không hết hạn
        return UserDetails.super.isAccountNonExpired(); // Sử dụng triển khai mặc định của UserDetails (thường là luôn true trừ khi có logic tùy chỉnh)
    }

    /**
     * Cho biết liệu tài khoản của người dùng có bị khóa hay không.
     * Phương thức này là một phần của giao diện UserDetails, được Spring Security sử dụng.
     *
     * @return true nếu tài khoản không bị khóa, ngược lại là false.
     */
    @Override // Ghi đè phương thức từ giao diện UserDetails
    public boolean isAccountNonLocked() { // Định nghĩa phương thức kiểm tra tài khoản không bị khóa
        return UserDetails.super.isAccountNonLocked(); // Sử dụng triển khai mặc định của UserDetails (thường là luôn true trừ khi có logic tùy chỉnh)
    }

    /**
     * Cho biết liệu thông tin xác thực (mật khẩu) của người dùng có hết hạn hay không.
     * Phương thức này là một phần của giao diện UserDetails, được Spring Security sử dụng.
     *
     * @return true nếu thông tin xác thực không hết hạn, ngược lại là false.
     */
    @Override // Ghi đè phương thức từ giao diện UserDetails
    public boolean isCredentialsNonExpired() { // Định nghĩa phương thức kiểm tra thông tin xác thực không hết hạn
        return UserDetails.super.isCredentialsNonExpired(); // Sử dụng triển khai mặc định của UserDetails (thường là luôn true trừ khi có logic tùy chỉnh)
    }

    /**
     * Cho biết liệu tài khoản của người dùng có được kích hoạt hay không.
     * Phương thức này là một phần của giao diện UserDetails, được Spring Security sử dụng.
     *
     * @return true nếu tài khoản được kích hoạt, ngược lại là false.
     */
    @Override // Ghi đè phương thức từ giao diện UserDetails
    public boolean isEnabled() { // Định nghĩa phương thức kiểm tra tài khoản có được kích hoạt không
        return UserDetails.super.isEnabled(); // Sử dụng triển khai mặc định của UserDetails (thường là luôn true trừ khi có logic tùy chỉnh)
    }
}
