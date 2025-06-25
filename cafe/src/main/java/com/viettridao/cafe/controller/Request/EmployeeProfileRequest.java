package com.viettridao.cafe.controller.Request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Class đại diện cho yêu cầu tạo hoặc cập nhật thông tin hồ sơ nhân viên. Sử
 * dụng để nhận dữ liệu từ form với các ràng buộc kiểm tra dữ liệu đầu vào.
 */
@Getter
@Setter
public class EmployeeProfileRequest {

	// Mã định danh duy nhất của nhân viên, có thể null khi tạo mới
	private Integer id;

	// Họ và tên đầy đủ của nhân viên, không được để trống và tối đa 30 ký tự
	@NotBlank(message = "Họ và tên không được để trống")
	@Size(max = 30, message = "Họ và tên không được vượt quá 30 ký tự")
	private String fullName;

	/**
	 * Chức vụ của nhân viên (ví dụ: Quản lý, Nhân viên phục vụ), không được để
	 * trống và tối đa 20 ký tự.
	 */
	@NotBlank(message = "Chức vụ không được để trống")
	@Size(max = 20, message = "Chức vụ không được vượt quá 20 ký tự")
	private String position;

	// Địa chỉ hiện tại của nhân viên, không được để trống và tối đa 50 ký tự
	@NotBlank(message = "Địa chỉ không được để trống")
	@Size(max = 50, message = "Địa chỉ không được vượt quá 50 ký tự")
	private String address;

	/**
	 * Số điện thoại của nhân viên. Phải là chuỗi số từ 10 đến 15 ký tự và không
	 * được để trống.
	 */
	@NotBlank(message = "Số điện thoại không được để trống")
	@Pattern(regexp = "\\d{10,15}", message = "Số điện thoại không hợp lệ (phải từ 10 đến 15 chữ số)")
	private String phoneNumber;

	/**
	 * Mức lương của nhân viên. Không được để trống và phải từ 1.000.000 VNĐ trở
	 * lên.
	 */
	@NotNull(message = "Lương không được để trống")
	@DecimalMin(value = "1000000", message = "Lương phải từ 1.000.000 VNĐ trở lên")
	private Double salary;

	// Đường dẫn đến hình ảnh đại diện của nhân viên, tối đa 255 ký tự, có thể null
	@Size(max = 255, message = "Link ảnh không vượt quá 255 ký tự")
	private String imageUrl;

	// Tên đăng nhập của nhân viên, không được để trống và tối đa 20 ký tự
	@NotBlank(message = "Tên đăng nhập không được để trống")
	@Size(max = 20, message = "Tên đăng nhập không được vượt quá 20 ký tự")
	private String username;

	// Mật khẩu của nhân viên, không được để trống và phải từ 6 đến 20 ký tự
	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 6, max = 20, message = "Mật khẩu phải từ 6 đến 20 ký tự")
	private String password;

	/**
	 * Kiểm tra logic: Nếu mật khẩu được cung cấp (không null và không rỗng), tên
	 * đăng nhập cũng phải được cung cấp.
	 *
	 * @return true nếu hợp lệ, false nếu vi phạm logic.
	 */
	@AssertTrue(message = "Nếu có mật khẩu thì tên đăng nhập không được để trống")
	public boolean isValidAccountLogic() {
		/**
		 * Logic kiểm tra: nếu mật khẩu không null và không rỗng, thì tên đăng nhập cũng
		 * không được null và không rỗng. Ngược lại (nếu mật khẩu null hoặc rỗng), thì
		 * điều kiện này luôn đúng.
		 */
		return password == null || password.isBlank() || (username != null && !username.isBlank());
	}
}