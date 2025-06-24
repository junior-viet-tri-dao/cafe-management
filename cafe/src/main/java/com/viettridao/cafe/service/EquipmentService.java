package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.controller.Request.EquipmentRequest;
import com.viettridao.cafe.controller.response.EquipmentResponse;
import com.viettridao.cafe.model.EquipmentEntity;

/**
 * Interface định nghĩa các dịch vụ liên quan đến quản lý thiết bị (Equipment).
 * Cung cấp các phương thức để thao tác với dữ liệu thiết bị, bao gồm truy vấn,
 * tạo, cập nhật, xóa và chuyển đổi dữ liệu.
 */
public interface EquipmentService {

    /**
     * Lấy danh sách tất cả các thiết bị đang hoạt động (chưa bị xóa mềm).
     * 
     * @return Danh sách các EquipmentEntity đang hoạt động
     */
    List<EquipmentEntity> getAllActive();

    /**
     * Lấy danh sách tất cả các thiết bị đang hoạt động dưới dạng phản hồi.
     * 
     * @return Danh sách các EquipmentResponse chứa thông tin thiết bị
     */
    List<EquipmentResponse> getAllActiveAsResponse();

    /**
     * Lấy thông tin thiết bị theo ID.
     * 
     * @param id ID của thiết bị cần truy vấn
     * @return EquipmentEntity tương ứng với ID
     * @throws RuntimeException nếu không tìm thấy thiết bị
     */
    EquipmentEntity getById(Integer id);

    /**
     * Tạo mới một thiết bị dựa trên thông tin yêu cầu.
     * 
     * @param request Đối tượng EquipmentRequest chứa thông tin thiết bị
     * @throws RuntimeException nếu dữ liệu yêu cầu không hợp lệ
     */
    void create(EquipmentRequest request);

    /**
     * Cập nhật thông tin thiết bị dựa trên yêu cầu.
     * 
     * @param request Đối tượng EquipmentRequest chứa thông tin cần cập nhật
     * @throws RuntimeException nếu thiết bị không tồn tại hoặc dữ liệu không hợp lệ
     */
    void update(EquipmentRequest request);

    /**
     * Xóa thiết bị (xóa mềm) theo ID.
     * 
     * @param id ID của thiết bị cần xóa
     * @throws RuntimeException nếu thiết bị không tồn tại
     */
    void delete(Integer id);

    /**
     * Chuyển đổi một EquipmentEntity thành EquipmentResponse.
     * 
     * @param entity Đối tượng EquipmentEntity cần chuyển đổi
     * @return EquipmentResponse chứa thông tin phản hồi
     */
    EquipmentResponse convertToResponse(EquipmentEntity entity);
}
