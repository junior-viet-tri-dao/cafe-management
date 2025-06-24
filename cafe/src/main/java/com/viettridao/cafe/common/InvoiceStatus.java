package com.viettridao.cafe.common;

/**
 * Enum định nghĩa các trạng thái có thể có của hóa đơn trong quán cà phê. Mỗi
 * trạng thái đại diện cho một giai đoạn hoặc tình trạng cụ thể của hóa đơn.
 */
public enum InvoiceStatus {
	// Hóa đơn chưa được thanh toán
	UNPAID,

	// Hóa đơn đã được thanh toán đầy đủ
	PAID,

	// Hóa đơn đang trong quá trình xử lý (ví dụ: kiểm tra thanh toán)
	PROCESSING,

	// Hóa đơn đã bị hủy
	CANCELLED,

	// Hóa đơn đang chờ thanh toán (ví dụ: chờ xác nhận giao dịch)
	PENDING,

	// Hóa đơn đã được hoàn tiền
	REFUNDED
}