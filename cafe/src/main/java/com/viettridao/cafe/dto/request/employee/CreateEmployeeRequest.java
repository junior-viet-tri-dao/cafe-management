package com.viettridao.cafe.dto.request.employee;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeRequest {

    // ID KHÔNG NÊN được gửi khi tạo mới.
    // Nếu có gửi, nó PHẢI là null. Nếu không phải null, đó là một lỗi logic.
//    @NotNull(message = "ID nhân viên không được cung cấp khi tạo mới")
//    private Integer id;

    @NotBlank(message = "Tên đầy đủ không được để trống")
    @Size(min = 2, max = 100, message = "Tên đầy đủ phải từ 2 đến 100 ký tự")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$",
            message = "Số điện thoại không hợp lệ (ví dụ: 0912345678, +84912345678)")
    private String phoneNumber;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(min = 5, max = 255, message = "Địa chỉ phải từ 5 đến 255 ký tự")
    private String address;

    // URL ảnh có thể trống hoặc là một URL hợp lệ.
    // Nếu bạn muốn nó là BẮT BUỘC, hãy đổi @Pattern thành @URL hoặc @NotBlank.
    // Hiện tại: cho phép null hoặc chuỗi rỗng nếu không có ảnh, hoặc phải là URL hợp lệ.
    @Pattern(regexp = "^(http|https)://.*\\.(jpeg|jpg|gif|png|webp|bmp)$|^$",
            message = "Đường dẫn ảnh không hợp lệ")
    private String imageUrl;

    @NotNull(message = "ID vị trí không được để trống")
    @Min(value = 1, message = "ID vị trí phải lớn hơn hoặc bằng 1")
    private Integer positionId;

//    @NotBlank(message = "Tên vị trí không được để trống")
//    @Size(min = 2, max = 50, message = "Tên vị trí phải từ 2 đến 50 ký tự")
//    private String positionName;

//    @NotNull(message = "Lương không được để trống")
//    @DecimalMin(value = "0.0", inclusive = true, message = "Lương phải là số không âm")
//    private Double salary;

    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 4, max = 50, message = "Tên đăng nhập phải từ 4 đến 50 ký tự")
    // @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Tên đăng nhập chỉ chứa chữ và số") // Tùy chọn: chỉ cho phép chữ và số
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải từ 6 đến 100 ký tự")
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{6,100}$",
    //          message = "Mật khẩu phải có ít nhất 1 chữ số, 1 chữ thường, 1 chữ hoa và 1 ký tự đặc biệt") // Tùy chọn: Mật khẩu mạnh
    private String password;

}
