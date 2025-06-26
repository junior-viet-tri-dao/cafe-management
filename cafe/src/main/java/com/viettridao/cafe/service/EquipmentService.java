package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.EquipmentRequest;
import com.viettridao.cafe.dto.response.EquipmentResponse;
import com.viettridao.cafe.model.EquipmentEntity;

/**
 * Giao diện định nghĩa các dịch vụ liên quan đến quản lý thiết bị (Equipment).
 * Cung cấp các phương thức để thao tác với dữ liệu thiết bị, bao gồm truy vấn,
 * tạo, cập nhật, xóa và chuyển đổi dữ liệu.
 */
public interface EquipmentService {

	/**
	 * Lấy danh sách tất cả các thiết bị đang hoạt động (chưa bị xóa mềm).
	 *
	 * @return Danh sách các EquipmentEntity đang hoạt động.
	 */
	List<EquipmentEntity> getAllActive();

	/**
	 * Lấy danh sách tất cả các thiết bị đang hoạt động dưới dạng phản hồi
	 * (Response). Phương thức này chuyển đổi EquipmentEntity thành
	 * EquipmentResponse để trả về client.
	 *
	 * @return Danh sách các EquipmentResponse chứa thông tin thiết bị.
	 */
	List<EquipmentResponse> getAllActiveAsResponse();

	/**
	 * Lấy thông tin chi tiết của một thiết bị theo ID.
	 *
	 * @param id ID của thiết bị cần truy vấn.
	 * @return EquipmentEntity tương ứng với ID đã cho.
	 * @throws RuntimeException nếu không tìm thấy thiết bị với ID được cung cấp.
	 */
	EquipmentEntity getById(Integer id);

	/**
	 * Tạo mới một thiết bị dựa trên thông tin được cung cấp trong đối tượng yêu
	 * cầu.
	 *
	 * @param request Đối tượng EquipmentRequest chứa thông tin thiết bị cần tạo.
	 * @throws RuntimeException nếu dữ liệu yêu cầu không hợp lệ hoặc có lỗi trong
	 *                          quá trình tạo.
	 */
	void create(EquipmentRequest request);

	/**
	 * Cập nhật thông tin của một thiết bị hiện có dựa trên dữ liệu từ yêu cầu.
	 *
	 * @param request Đối tượng EquipmentRequest chứa thông tin cần cập nhật, bao
	 *                gồm ID của thiết bị.
	 * @throws RuntimeException nếu thiết bị không tồn tại hoặc dữ liệu cập nhật
	 *                          không hợp lệ.
	 */
	void update(EquipmentRequest request);

	/**
	 * Xóa một thiết bị theo ID của nó. Phương pháp này thường thực hiện "xóa mềm"
	 * (soft delete) bằng cách cập nhật trạng thái `isDeleted` thay vì xóa vĩnh viễn
	 * khỏi cơ sở dữ liệu.
	 *
	 * @param id ID của thiết bị cần xóa.
	 * @throws RuntimeException nếu thiết bị không tồn tại.
	 */
	void delete(Integer id);

	/**
	 * Chuyển đổi một đối tượng EquipmentEntity sang đối tượng EquipmentResponse.
	 * Được sử dụng để chuẩn bị dữ liệu entity cho việc trả về client dưới dạng phản
	 * hồi.
	 *
	 * @param entity Đối tượng EquipmentEntity cần chuyển đổi.
	 * @return EquipmentResponse chứa thông tin phản hồi đã được định dạng.
	 */
	EquipmentResponse convertToResponse(EquipmentEntity entity);
}