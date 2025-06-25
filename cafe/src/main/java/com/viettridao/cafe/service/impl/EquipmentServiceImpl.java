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
@Service // Đánh dấu lớp này là một Spring Service, thành phần trong tầng dịch vụ
public class EquipmentServiceImpl implements EquipmentService {

	// Inject EquipmentRepository để tương tác với dữ liệu thiết bị trong cơ sở dữ
	// liệu
	@Autowired
	private EquipmentRepository equipmentRepository;

	/**
	 * Lấy danh sách tất cả thiết bị đang hoạt động (chưa bị xóa logic).
	 *
	 * @return Danh sách EquipmentEntity của các thiết bị chưa bị xóa.
	 * @throws RuntimeException nếu có lỗi khi truy vấn danh sách.
	 */
	@Override
	public List<EquipmentEntity> getAllActive() {
		try {
			// Truy vấn danh sách thiết bị với isDeleted = false từ repository
			return equipmentRepository.findAllByIsDeletedFalse();
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Không thể lấy danh sách thiết bị: " + e.getMessage());
		}
	}

	/**
	 * Lấy danh sách tất cả thiết bị đang hoạt động dưới dạng đối tượng phản hồi
	 * (Response). Phương thức này truy vấn các entity và sau đó chuyển đổi chúng
	 * sang dạng Response.
	 *
	 * @return Danh sách EquipmentResponse của các thiết bị chưa bị xóa.
	 * @throws RuntimeException nếu có lỗi khi chuyển đổi dữ liệu.
	 */
	@Override
	public List<EquipmentResponse> getAllActiveAsResponse() {
		try {
			// Truy vấn danh sách thiết bị chưa bị xóa và sử dụng Stream API
			// để ánh xạ (map) từng EquipmentEntity sang EquipmentResponse
			return equipmentRepository.findAllByIsDeletedFalse().stream().map(this::convertToResponse)
					.collect(Collectors.toList()); // Thu thập kết quả vào một List mới
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Không thể chuyển đổi thiết bị sang Response: " + e.getMessage());
		}
	}

	/**
	 * Tìm thiết bị theo ID.
	 *
	 * @param id ID của thiết bị cần tìm.
	 * @return EquipmentEntity nếu tìm thấy.
	 * @throws RuntimeException nếu không tìm thấy thiết bị với ID đã cho hoặc có
	 *                          lỗi trong quá trình truy vấn.
	 */
	@Override
	public EquipmentEntity getById(Integer id) {
		try {
			// Tìm thiết bị theo ID bằng repository.
			// Nếu không tìm thấy, ném ngoại lệ RuntimeException với thông báo rõ ràng.
			return equipmentRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy thiết bị có ID: " + id));
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Lỗi khi tìm thiết bị: " + e.getMessage());
		}
	}

	/**
	 * Tạo mới một thiết bị dựa trên thông tin được cung cấp trong đối tượng yêu cầu
	 * (Request).
	 *
	 * @param request Đối tượng EquipmentRequest chứa thông tin thiết bị cần tạo.
	 * @throws RuntimeException nếu có lỗi khi lưu thiết bị vào cơ sở dữ liệu.
	 */
	@Override
	public void create(EquipmentRequest request) {
		try {
			// Khởi tạo một thực thể EquipmentEntity mới
			EquipmentEntity entity = new EquipmentEntity();
			// Gán các thuộc tính từ đối tượng request vào entity
			entity.setEquipmentName(request.getEquipmentName());
			entity.setPurchaseDate(request.getPurchaseDate()); // Gán ngày mua thiết bị
			entity.setQuantity(request.getQuantity());
			entity.setPurchasePrice(request.getPurchasePrice());
			entity.setNotes(request.getNotes());
			entity.setIsDeleted(false); // Đặt trạng thái ban đầu là chưa bị xóa logic

			// Lưu thực thể thiết bị mới vào cơ sở dữ liệu
			equipmentRepository.save(entity);
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Thêm thiết bị thất bại: " + e.getMessage());
		}
	}

	/**
	 * Cập nhật thông tin thiết bị dựa trên dữ liệu từ yêu cầu (Request).
	 *
	 * @param request Đối tượng EquipmentRequest chứa thông tin thiết bị cần cập
	 *                nhật, bao gồm ID.
	 * @throws RuntimeException nếu không tìm thấy thiết bị để cập nhật hoặc có lỗi
	 *                          trong quá trình lưu.
	 */
	@Override
	public void update(EquipmentRequest request) {
		try {
			// Tìm thiết bị hiện có trong cơ sở dữ liệu bằng ID từ request
			EquipmentEntity entity = getById(request.getId()); // Sử dụng phương thức getById đã định nghĩa

			// Cập nhật các thuộc tính của entity với dữ liệu từ request
			entity.setEquipmentName(request.getEquipmentName());
			entity.setPurchaseDate(request.getPurchaseDate()); // Cập nhật ngày mua thiết bị
			entity.setQuantity(request.getQuantity());
			entity.setPurchasePrice(request.getPurchasePrice());
			entity.setNotes(request.getNotes());

			// Lưu các thay đổi vào cơ sở dữ liệu
			equipmentRepository.save(entity);
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Cập nhật thiết bị thất bại: " + e.getMessage());
		}
	}

	/**
	 * Xóa logic một thiết bị bằng cách đặt thuộc tính `isDeleted` của nó thành
	 * `true`. Điều này giữ lại bản ghi trong cơ sở dữ liệu nhưng đánh dấu nó là
	 * không hoạt động.
	 *
	 * @param id ID của thiết bị cần xóa.
	 * @throws RuntimeException nếu không tìm thấy thiết bị để xóa hoặc có lỗi trong
	 *                          quá trình lưu.
	 */
	@Override
	public void delete(Integer id) {
		try {
			// Tìm thiết bị hiện có trong cơ sở dữ liệu bằng ID
			EquipmentEntity entity = getById(id); // Sử dụng phương thức getById đã định nghĩa

			// Đặt trạng thái isDeleted thành true để thực hiện xóa logic (soft delete)
			entity.setIsDeleted(true);

			// Lưu thực thể đã cập nhật vào cơ sở dữ liệu
			equipmentRepository.save(entity);
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Xóa thiết bị thất bại: " + e.getMessage());
		}
	}

	/**
	 * Chuyển đổi một thực thể thiết bị (EquipmentEntity) sang đối tượng phản hồi
	 * (EquipmentResponse). Phương thức này được sử dụng để chuẩn bị dữ liệu cho
	 * việc gửi về phía client.
	 *
	 * @param entity Thực thể thiết bị cần chuyển đổi.
	 * @return EquipmentResponse chứa thông tin thiết bị đã được định dạng để phản
	 *         hồi.
	 * @throws RuntimeException nếu có lỗi trong quá trình chuyển đổi.
	 */
	@Override
	public EquipmentResponse convertToResponse(EquipmentEntity entity) {
		try {
			// Khởi tạo một đối tượng EquipmentResponse mới
			EquipmentResponse res = new EquipmentResponse();
			// Gán các thuộc tính từ entity vào response
			res.setId(entity.getId());
			res.setEquipmentName(entity.getEquipmentName());
			res.setPurchaseDate(entity.getPurchaseDate()); // Gán ngày mua thiết bị
			res.setQuantity(entity.getQuantity());
			res.setPurchasePrice(entity.getPurchasePrice());
			// Tính tổng giá trị (số lượng * giá mua) và gán vào response
			res.setTotalPrice(entity.getQuantity() * entity.getPurchasePrice());
			res.setNotes(entity.getNotes());
			return res;
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Chuyển đổi thiết bị thất bại: " + e.getMessage());
		}
	}
}