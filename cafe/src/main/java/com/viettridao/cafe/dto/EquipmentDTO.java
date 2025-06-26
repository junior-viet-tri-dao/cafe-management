package com.viettridao.cafe.dto;

import java.time.LocalDate; 

import lombok.AllArgsConstructor; 
import lombok.Getter; 
import lombok.Setter; 

/**
 * Lớp DTO (Data Transfer Object) dùng để truyền tải thông tin thiết bị. Chứa
 * các thuộc tính cơ bản của một thiết bị và một phương thức tính toán tổng giá.
 * Được sử dụng để gửi dữ liệu thiết bị giữa các lớp (ví dụ: từ Entity sang
 * Response, hoặc từ Service sang Controller).
 */
@Getter
@Setter
@AllArgsConstructor // Tự động tạo constructor với tất cả các thuộc tính
public class EquipmentDTO {
	// Mã định danh duy nhất của thiết bị
	private Integer id;
	// Tên của thiết bị
	private String equipmentName;
	// Ngày mua thiết bị
	private LocalDate purchaseDate;
	// Số lượng của thiết bị
	private Integer quantity;
	// Đơn giá mua của thiết bị
	private Double purchasePrice;
	// Ghi chú thêm về thiết bị
	private String notes;

	/**
	 * Tính toán tổng giá trị của thiết bị (Số lượng * Đơn giá mua).
	 *
	 * @return Tổng giá trị của thiết bị; trả về 0.0 nếu số lượng hoặc đơn giá chưa
	 *         được thiết lập.
	 */
	public Double getTotalPrice() {
		// Kiểm tra nếu số lượng và đơn giá không rỗng để tránh NullPointerException
		if (quantity != null && purchasePrice != null) {
			return quantity * purchasePrice; // Trả về tổng giá
		}
		return 0.0; // Trả về 0.0 nếu không có đủ thông tin để tính toán
	}
}