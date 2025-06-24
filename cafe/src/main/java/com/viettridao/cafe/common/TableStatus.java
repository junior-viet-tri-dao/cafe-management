package com.viettridao.cafe.common;

/**
 * Enum định nghĩa các trạng thái có thể có của bàn trong quán cà phê. Mỗi trạng
 * thái đại diện cho một tình trạng cụ thể của bàn.
 */
public enum TableStatus {
	// Bàn trống, sẵn sàng để khách sử dụng
	AVAILABLE,

	// Bàn đang được khách sử dụng
	OCCUPIED,

	// Bàn đã được đặt trước bởi khách
	RESERVED,

	// Bàn không hoạt động hoặc ngừng phục vụ
	OUT_OF_SERVICE
}