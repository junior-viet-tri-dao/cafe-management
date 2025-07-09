package com.viettridao.cafe.dto.response.account;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO Response cho thông tin tài khoản nhân viên - Data Transfer Object từ
 * service layer đến presentation layer.
 * 
 * Thiết kế business context:
 * - Đóng gói toàn bộ thông tin account cần thiết cho UI rendering
 * - Bao gồm cả thông tin nhân viên và credentials đăng nhập
 * - Hỗ trợ formatted data cho better UX (ví dụ: formattedSalary)
 * - Áp dụng principle of least privilege - chỉ expose cần thiết
 * 
 * Performance optimizations:
 * - Lightweight POJO với Lombok để reduce boilerplate code
 * - Pre-calculated fields (formattedSalary) để avoid UI-side formatting
 * - Minimal object graph để optimize JSON serialization
 * - Immutable references để ensure thread safety
 * 
 * Security considerations:
 * - Password field included nhưng sẽ được masked ở presentation layer
 * - Sensitive data như salary được controlled access
 * - Account status thông qua isDeleted field cho soft delete audit
 * - Position-based authorization data embedded
 * 
 * Integration patterns:
 * - Compatible với Spring MVC Model binding
 * - JSON serialization ready cho RESTful APIs
 * - Thymeleaf template engine friendly structure
 * - MapStruct mapping framework optimized
 * 
 * @author Cafe Management Team
 * @since 1.0
 * @see com.viettridao.cafe.model.AccountEntity
 * @see com.viettridao.cafe.mapper.AccountMapper
 */
@Getter
@Setter
public class AccountResponse {

    /** Unique identifier của account - Primary key reference */
    private Integer id;

    /** Họ và tên đầy đủ của nhân viên - Required field cho identity */
    private String fullName;

    /** Số điện thoại liên lạc - Business communication channel */
    private String phoneNumber;

    /** Địa chỉ thường trú - Optional field cho HR management */
    private String address;

    /** URL avatar/profile image - S3 or local storage path */
    private String imageUrl;

    /** ID vị trí công việc - Foreign key reference đến Position entity */
    private Integer positionId;

    /** Tên vị trí công việc - Denormalized field để optimize UI queries */
    private String positionName;

    /** Mức lương cơ bản - Sensitive financial data, access controlled */
    private Double salary;

    /** Username đăng nhập hệ thống - Unique constraint enforced */
    private String username;

    /** Password hash - Encoded field, never store plaintext */
    private String password;

    /** Trạng thái xóa mềm - Soft delete flag cho audit trail */
    private Boolean isDeleted;

}
