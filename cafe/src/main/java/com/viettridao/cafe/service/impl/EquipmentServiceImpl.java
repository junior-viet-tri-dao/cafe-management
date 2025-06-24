package com.viettridao.cafe.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.controller.Request.EquipmentRequest;
import com.viettridao.cafe.controller.response.EquipmentResponse;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.repository.EquipmentRepository;
import com.viettridao.cafe.service.EquipmentService;

/**
 * Lớp triển khai dịch vụ quản lý thiết bị. Cung cấp các phương thức để lấy,
 * tạo, cập nhật, xóa và chuyển đổi thông tin thiết bị.
 */
@Service
public class EquipmentServiceImpl implements EquipmentService {

	// Repository để thao tác với dữ liệu thiết bị trong cơ sở dữ liệu
	@Autowired
	private EquipmentRepository equipmentRepository;

	/**
	 * Lấy danh sách tất cả thiết bị đang hoạt động (chưa bị xóa logic).
	 * 
	 * @return Danh sách EquipmentEntity của các thiết bị chưa bị xóa
	 * @throws RuntimeException nếu có lỗi khi truy vấn danh sách
	 */
	@Override
	public List<EquipmentEntity> getAllActive() {
		try {
			// Truy vấn danh sách thiết bị với isDeleted = false
			return equipmentRepository.findAllByIsDeletedFalse();
		} catch (Exception e) {
			throw new RuntimeException("Không thể lấy danh sách thiết bị: " + e.getMessage());
		}
	}

	/**
	 * Lấy danh sách tất cả thiết bị đang hoạt động dưới dạng response.
	 * 
	 * @return Danh sách EquipmentResponse của các thiết bị chưa bị xóa
	 * @throws RuntimeException nếu có lỗi khi chuyển đổi dữ liệu
	 */
	@Override
	public List<EquipmentResponse> getAllActiveAsResponse() {
		try {
			// Truy vấn danh sách thiết bị và chuyển đổi từng entity sang response
			return equipmentRepository.findAllByIsDeletedFalse().stream().map(this::convertToResponse)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException("Không thể chuyển đổi thiết bị sang Response: " + e.getMessage());
		}
	}

	/**
	 * Tìm thiết bị theo ID.
	 * 
	 * @param id ID của thiết bị cần tìm
	 * @return EquipmentEntity nếu tìm thấy
	 * @throws RuntimeException nếu không tìm thấy thiết bị hoặc có lỗi
	 */
	@Override
	public EquipmentEntity getById(Integer id) {
		try {
			// Tìm thiết bị theo ID, ném ngoại lệ nếu không tìm thấy
			return equipmentRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị có ID: " + id));
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi tìm thiết bị: " + e.getMessage());
		}
	}

	/**
	 * Tạo mới một thiết bị dựa trên yêu cầu.
	 * 
	 * @param request Đối tượng chứa thông tin thiết bị cần tạo
	 * @throws RuntimeException nếu có lỗi khi lưu thiết bị
	 */
	@Override
	public void create(EquipmentRequest request) {
		try {
			// Tạo thực thể thiết bị mới
			EquipmentEntity entity = new EquipmentEntity();
			// Gán các thuộc tính từ request
			entity.setEquipmentName(request.getEquipmentName());
			entity.setPurchaseDate(request.getPurchaseDate());
			entity.setQuantity(request.getQuantity());
			entity.setPurchasePrice(request.getPurchasePrice());
			entity.setNotes(request.getNotes());
			entity.setIsDeleted(false); // Đặt trạng thái chưa xóa
			// Lưu thiết bị vào cơ sở dữ liệu
			equipmentRepository.save(entity);
		} catch (Exception e) {
			throw new RuntimeException("Thêm thiết bị thất bại: " + e.getMessage());
		}
	}

	/**
	 * Cập nhật thông tin thiết bị dựa trên yêu cầu.
	 * 
	 * @param request Đối tượng chứa thông tin thiết bị cần cập nhật
	 * @throws RuntimeException nếu không tìm thấy thiết bị hoặc có lỗi khi cập nhật
	 */
	@Override
	public void update(EquipmentRequest request) {
		try {
			// Tìm thiết bị theo ID
			EquipmentEntity entity = getById(request.getId());
			// Cập nhật các thuộc tính từ request
			entity.setEquipmentName(request.getEquipmentName());
			entity.setPurchaseDate(request.getPurchaseDate());
			entity.setQuantity(request.getQuantity());
			entity.setPurchasePrice(request.getPurchasePrice());
			entity.setNotes(request.getNotes());
			// Lưu thiết bị đã cập nhật vào cơ sở dữ liệu
			equipmentRepository.save(entity);
		} catch (Exception e) {
			throw new RuntimeException("Cập nhật thiết bị thất bại: " + e.getMessage());
		}
	}

	/**
	 * Xóa logic một thiết bị bằng cách đặt isDeleted = true.
	 * 
	 * @param id ID của thiết bị cần xóa
	 * @throws RuntimeException nếu không tìm thấy thiết bị hoặc có lỗi khi xóa
	 */
	@Override
	public void delete(Integer id) {
		try {
			// Tìm thiết bị theo ID
			EquipmentEntity entity = getById(id);
			// Đặt trạng thái xóa logic
			entity.setIsDeleted(true);
			// Lưu thiết bị đã cập nhật vào cơ sở dữ liệu
			equipmentRepository.save(entity);
		} catch (Exception e) {
			throw new RuntimeException("Xóa thiết bị thất bại: " + e.getMessage());
		}
	}

	/**
	 * Chuyển đổi thực thể thiết bị sang đối tượng response.
	 * 
	 * @param entity Thực thể thiết bị cần chuyển đổi
	 * @return EquipmentResponse chứa thông tin thiết bị
	 * @throws RuntimeException nếu có lỗi khi chuyển đổi
	 */
	@Override
	public EquipmentResponse convertToResponse(EquipmentEntity entity) {
		try {
			// Tạo đối tượng response
			EquipmentResponse res = new EquipmentResponse();
			// Gán các thuộc tính từ entity
			res.setId(entity.getId());
			res.setEquipmentName(entity.getEquipmentName());
			res.setPurchaseDate(entity.getPurchaseDate());
			res.setQuantity(entity.getQuantity());
			res.setPurchasePrice(entity.getPurchasePrice());
			// Tính tổng giá trị (số lượng * giá mua)
			res.setTotalPrice(entity.getQuantity() * entity.getPurchasePrice());
			res.setNotes(entity.getNotes());
			return res;
		} catch (Exception e) {
			throw new RuntimeException("Chuyển đổi thiết bị thất bại: " + e.getMessage());
		}
	}
}