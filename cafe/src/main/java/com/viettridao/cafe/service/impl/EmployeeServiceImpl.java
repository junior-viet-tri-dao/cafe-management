package com.viettridao.cafe.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.controller.response.EmployeeProfileResponse;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.EmployeeService;

/**
 * Lớp triển khai dịch vụ quản lý nhân viên (EmployeeService). Cung cấp các
 * phương thức để thực hiện các thao tác CRUD và các nghiệp vụ liên quan đến
 * nhân viên.
 */
@Service // Đánh dấu lớp này là một Spring Service, thành phần trong tầng dịch vụ
public class EmployeeServiceImpl implements EmployeeService {

	// Inject EmployeeRepository để tương tác với cơ sở dữ liệu cho thực thể
	// Employee
	@Autowired
	private EmployeeRepository employeeRepository;

	// Inject PasswordEncoder để mã hóa mật khẩu
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Lấy thông tin hồ sơ nhân viên dưới dạng đối tượng phản hồi (Response) dựa
	 * trên tên đăng nhập.
	 *
	 * @param username Tên đăng nhập của nhân viên.
	 * @return EmployeeProfileResponse chứa thông tin hồ sơ của nhân viên.
	 */
	@Override
	public EmployeeProfileResponse getProfileByUsername(String username) {
		// Tìm nhân viên theo username và sau đó chuyển đổi sang đối tượng Response
		return convertToResponse(findByUsername(username));
	}

	/**
	 * Cập nhật thông tin hồ sơ nhân viên dựa trên dữ liệu từ đối tượng yêu cầu
	 * (Request).
	 *
	 * @param request Đối tượng EmployeeProfileRequest chứa thông tin cần cập nhật.
	 * @throws RuntimeException nếu không tìm thấy nhân viên hoặc có lỗi trong quá
	 *                          trình cập nhật.
	 */
	@Override
	public void updateProfile(EmployeeProfileRequest request) {
		try {
			// Tìm nhân viên trong cơ sở dữ liệu bằng ID. Nếu không tìm thấy, ném ngoại lệ.
			EmployeeEntity employee = employeeRepository.findById(request.getId())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

			// Cập nhật thông tin cơ bản của nhân viên
			employee.setFullName(request.getFullName());
			employee.setAddress(request.getAddress());
			employee.setPhoneNumber(request.getPhoneNumber());

			// Cập nhật thông tin chức vụ và lương nếu có
			if (employee.getPosition() != null) {
				employee.getPosition().setPositionName(request.getPosition());
				employee.getPosition().setSalary(request.getSalary());
			}

			// Cập nhật thông tin tài khoản nếu có
			if (employee.getAccount() != null) {
				employee.getAccount().setUsername(request.getUsername());

				// Cập nhật mật khẩu nếu mật khẩu mới được cung cấp và không rỗng
				if (request.getPassword() != null && !request.getPassword().isBlank()) {
					employee.getAccount().setPassword(passwordEncoder.encode(request.getPassword()));
				}

				// Cập nhật URL ảnh đại diện nếu ảnh mới được cung cấp và không rỗng
				if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
					employee.getAccount().setImageUrl(request.getImageUrl());
				}
			}

			// Lưu các thay đổi vào cơ sở dữ liệu
			employeeRepository.save(employee);
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Lỗi khi cập nhật nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Tìm kiếm một thực thể nhân viên (EmployeeEntity) dựa trên tên đăng nhập của
	 * tài khoản liên kết.
	 *
	 * @param username Tên đăng nhập của nhân viên cần tìm.
	 * @return EmployeeEntity tìm thấy.
	 * @throws RuntimeException nếu không tìm thấy nhân viên với tên đăng nhập đã
	 *                          cho.
	 */
	@Override
	public EmployeeEntity findByUsername(String username) {
		// Gọi phương thức từ repository để tìm nhân viên theo username tài khoản.
		// Nếu không tìm thấy, ném ngoại lệ.
		return employeeRepository.findByAccount_Username(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));
	}

	/**
	 * Chuyển đổi một thực thể EmployeeEntity sang đối tượng phản hồi
	 * EmployeeProfileResponse.
	 *
	 * @param employee Thực thể EmployeeEntity cần chuyển đổi.
	 * @return Đối tượng EmployeeProfileResponse đã được chuyển đổi.
	 * @throws RuntimeException nếu có lỗi trong quá trình chuyển đổi.
	 */
	@Override
	public EmployeeProfileResponse convertToResponse(EmployeeEntity employee) {
		EmployeeProfileResponse res = new EmployeeProfileResponse();

		try {
			// Gán các thuộc tính cơ bản
			res.setId(employee.getId());
			res.setFullName(employee.getFullName());
			res.setAddress(employee.getAddress());
			res.setPhoneNumber(employee.getPhoneNumber());

			// Gán thông tin chức vụ và lương nếu có
			if (employee.getPosition() != null) {
				res.setPosition(employee.getPosition().getPositionName());
				res.setSalary(employee.getPosition().getSalary());
			}

			// Gán thông tin tài khoản nếu có
			if (employee.getAccount() != null) {
				res.setUsername(employee.getAccount().getUsername());
				res.setPassword(employee.getAccount().getPassword()); // Cẩn trọng khi trả về mật khẩu
				res.setImageUrl(employee.getAccount().getImageUrl());
			}

		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Lỗi khi chuyển đổi thông tin nhân viên: " + e.getMessage());
		}

		return res;
	}

	/**
	 * Tạo một nhân viên mới dựa trên dữ liệu từ đối tượng yêu cầu (Request). Bao
	 * gồm cả việc tạo thông tin chức vụ và tài khoản liên kết.
	 *
	 * @param request Đối tượng EmployeeProfileRequest chứa thông tin để tạo nhân
	 *                viên.
	 * @throws RuntimeException nếu có lỗi trong quá trình tạo nhân viên mới.
	 */
	@Override
	public void create(EmployeeProfileRequest request) {
		try {
			EmployeeEntity employee = new EmployeeEntity();
			employee.setFullName(request.getFullName());
			employee.setAddress(request.getAddress());
			employee.setPhoneNumber(request.getPhoneNumber());
			employee.setIsDeleted(false); // Thiết lập trạng thái chưa bị xóa logic

			// Tạo và thiết lập thông tin chức vụ cho nhân viên
			PositionEntity position = new PositionEntity();
			position.setPositionName(request.getPosition());
			position.setSalary(request.getSalary());
			position.setIsDeleted(false); // Thiết lập trạng thái chưa bị xóa logic
			employee.setPosition(position);

			// Tạo và thiết lập thông tin tài khoản cho nhân viên
			AccountEntity account = new AccountEntity();
			account.setUsername(request.getUsername());
			account.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa mật khẩu trước khi lưu
			account.setImageUrl(request.getImageUrl());
			account.setPermission("EMPLOYEE"); // Gán quyền mặc định là "EMPLOYEE"
			account.setIsDeleted(false); // Thiết lập trạng thái chưa bị xóa logic
			employee.setAccount(account);

			// Lưu thực thể nhân viên mới vào cơ sở dữ liệu
			employeeRepository.save(employee);
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Lỗi khi tạo nhân viên mới: " + e.getMessage());
		}
	}

	/**
	 * Xóa một nhân viên theo ID của họ bằng cách đánh dấu `isDeleted` là `true`
	 * (xóa mềm).
	 *
	 * @param id ID của nhân viên cần xóa.
	 * @throws RuntimeException nếu không tìm thấy nhân viên hoặc có lỗi trong quá
	 *                          trình xóa.
	 */
	@Override
	public void deleteById(Integer id) {
		try {
			// Tìm nhân viên theo ID. Nếu không tìm thấy, ném ngoại lệ.
			EmployeeEntity employee = employeeRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
			// Đánh dấu nhân viên là đã xóa mềm
			employee.setIsDeleted(true);
			// Lưu lại trạng thái đã cập nhật
			employeeRepository.save(employee);
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Lỗi khi xóa nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Lấy danh sách tất cả các nhân viên đang hoạt động (có `isDeleted` là
	 * `false`). Kết quả được chuyển đổi sang danh sách các đối tượng phản hồi.
	 *
	 * @return List các EmployeeProfileResponse của các nhân viên hoạt động.
	 * @throws RuntimeException nếu có lỗi khi lấy danh sách.
	 */
	@Override
	public List<EmployeeProfileResponse> getAllActive() {
		try {
			// Lấy tất cả nhân viên chưa bị xóa logic từ repository
			// Sau đó, sử dụng Stream API để chuyển đổi từng EmployeeEntity sang
			// EmployeeProfileResponse
			return employeeRepository.findAllByIsDeletedFalse().stream().map(this::convertToResponse)
					.collect(Collectors.toList());
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Tìm kiếm nhân viên dựa trên một từ khóa, có thể tìm theo tên đầy đủ, tên chức
	 * vụ hoặc mức lương.
	 *
	 * @param keyword Từ khóa dùng để tìm kiếm.
	 * @return List các EmployeeProfileResponse khớp với từ khóa tìm kiếm.
	 * @throws RuntimeException nếu có lỗi trong quá trình tìm kiếm.
	 */
	@Override
	public List<EmployeeProfileResponse> search(String keyword) {
		try {
			List<EmployeeEntity> results;
			try {
				// Cố gắng chuyển đổi từ khóa thành số để tìm kiếm theo lương
				Double salary = Double.parseDouble(keyword);
				results = employeeRepository.findByIsDeletedFalseAndPosition_Salary(salary);
			} catch (NumberFormatException e) {
				// Nếu không phải số, tìm kiếm theo tên đầy đủ hoặc tên chức vụ (không phân biệt
				// chữ hoa/thường)
				results = employeeRepository
						.findByIsDeletedFalseAndFullNameContainingIgnoreCaseOrPosition_PositionNameContainingIgnoreCase(
								keyword, keyword);
			}
			// Chuyển đổi kết quả tìm kiếm sang danh sách các đối tượng Response
			return results.stream().map(this::convertToResponse).collect(Collectors.toList());
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Lỗi khi tìm kiếm nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Lấy một thực thể nhân viên (EmployeeEntity) theo ID của họ.
	 *
	 * @param id ID của nhân viên cần tìm.
	 * @return EmployeeEntity tìm thấy.
	 * @throws RuntimeException nếu không tìm thấy nhân viên với ID đã cho.
	 */
	@Override
	public EmployeeEntity getById(Integer id) {
		// Tìm nhân viên theo ID. Nếu không tìm thấy, ném ngoại lệ.
		return employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + id));
	}

	/**
	 * Lấy thông tin hồ sơ nhân viên dưới dạng DTO (Data Transfer Object) dựa trên
	 * tên đăng nhập. Được sử dụng để truyền tải dữ liệu hồ sơ một cách gọn gàng.
	 *
	 * @param username Tên đăng nhập của nhân viên.
	 * @return EmployeeProfileDTO chứa thông tin hồ sơ của nhân viên.
	 * @throws RuntimeException nếu có lỗi khi lấy thông tin DTO.
	 */
	@Override
	public EmployeeProfileDTO getProfileDTO(String username) {
		try {
			// Tìm thực thể nhân viên bằng username
			EmployeeEntity employee = findByUsername(username);
			// Tạo và trả về đối tượng EmployeeProfileDTO từ thông tin của EmployeeEntity
			return new EmployeeProfileDTO(employee.getId(), employee.getFullName(),
					// Kiểm tra null cho Position trước khi truy cập PositionName
					employee.getPosition() != null ? employee.getPosition().getPositionName() : null,
					employee.getAddress(), employee.getPhoneNumber(),
					// Kiểm tra null cho Position trước khi truy cập Salary
					employee.getPosition() != null ? employee.getPosition().getSalary() : null,
					// Kiểm tra null cho Account trước khi truy cập Username
					employee.getAccount() != null ? employee.getAccount().getUsername() : null,
					// Kiểm tra null cho Account trước khi truy cập Password
					employee.getAccount() != null ? employee.getAccount().getPassword() : null,
					// Kiểm tra null cho Account trước khi truy cập ImageUrl
					employee.getAccount() != null ? employee.getAccount().getImageUrl() : null);
		} catch (Exception e) {
			// Bắt và ném lại các ngoại lệ với thông báo rõ ràng hơn
			throw new RuntimeException("Lỗi khi lấy thông tin DTO nhân viên: " + e.getMessage());
		}
	}
}