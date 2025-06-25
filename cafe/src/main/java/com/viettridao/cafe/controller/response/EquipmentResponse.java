package com.viettridao.cafe.controller.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * Lớp đại diện cho cấu trúc dữ liệu phản hồi (response) khi truy vấn thông tin
 * thiết bị. Dùng để trả về thông tin chi tiết của một thiết bị cho phía client.
 */
@Getter
@Setter
public class EquipmentResponse {
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

	// Tổng giá trị của số lượng thiết bị (quantity * purchasePrice)
	private Double totalPrice;

	// Ghi chú thêm về thiết bị
	private String notes;
}