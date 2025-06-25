package com.viettridao.cafe.controller.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Class đại diện cho yêu cầu tạo hoặc cập nhật thông tin thiết bị. Sử dụng để
 * nhận dữ liệu từ form với các ràng buộc kiểm tra dữ liệu đầu vào.
 */
@Getter
@Setter
public class EquipmentRequest {

	// Mã định danh duy nhất của thiết bị, có thể null khi tạo mới
	private Integer id;

	// Tên thiết bị, không được để trống và tối đa 50 ký tự
	@NotBlank(message = "Tên thiết bị không được để trống")
	@Size(max = 50, message = "Tên thiết bị không được vượt quá 50 ký tự")
	private String equipmentName;

	/**
	 * Ngày mua thiết bị, không được để trống và phải là ngày hợp lệ Ngày mua thiết
	 * bị, không được để trống và phải là một ngày hợp lệ (không trong tương lai)
	 */
	@NotNull(message = "Ngày mua không được để trống")
	private LocalDate purchaseDate;

	// Số lượng thiết bị, không được để trống và phải lớn hơn 0
	@NotNull(message = "Số lượng không được để trống")
	@DecimalMin(value = "1", message = "Số lượng phải lớn hơn 0")
	private Integer quantity;

	// Đơn giá mua thiết bị, không được để trống và phải lớn hơn 1.000 VNĐ
	@NotNull(message = "Đơn giá không được để trống")
	@DecimalMin(value = "1000", message = "Đơn giá phải lớn hơn 1.000 VNĐ")
	private Double purchasePrice;

	// Ghi chú về thiết bị, tối đa 255 ký tự, có thể null
	@Size(max = 255, message = "Ghi chú không vượt quá 255 ký tự")
	private String notes;

	/**
	 * Kiểm tra logic: Ngày mua thiết bị không được ở tương lai.
	 *
	 * @return true nếu ngày mua hợp lệ (không ở tương lai), false nếu vi phạm.
	 */
	/**
	 * Phương thức kiểm tra logic: Đảm bảo ngày mua không ở trong tương lai. Trả về
	 * true nếu ngày mua hợp lệ (null hoặc không sau ngày hiện tại), ngược lại trả
	 * về false.
	 */
	@AssertTrue(message = "Ngày mua không được ở tương lai")
	public boolean isPurchaseDateValid() {
		// Nếu purchaseDate null hoặc không ở tương lai, trả về true
		return purchaseDate == null || !purchaseDate.isAfter(LocalDate.now());
	}
}