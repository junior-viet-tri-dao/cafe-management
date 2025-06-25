package com.viettridao.cafe.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.controller.Request.ProfileUpdateRequest;
import com.viettridao.cafe.controller.response.EmployeeProfileResponse;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.EmployeeService;

/**
 * Lớp triển khai dịch vụ quản lý thông tin nhân viên. Xử lý các nghiệp vụ như
 * tạo, cập nhật, xóa, tìm kiếm và lấy thông tin nhân viên.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

	// Repository để tương tác với cơ sở dữ liệu nhân viên
	@Autowired
	private EmployeeRepository employeeRepository;

	// Công cụ mã hóa mật khẩu
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Cập nhật thông tin hồ sơ nhân viên dựa trên yêu cầu từ form.
	 * 
	 * @param request Đối tượng EmployeeProfileRequest chứa thông tin cần cập nhật.
	 */
	@Override
	public void updateProfile(EmployeeProfileRequest request) {
		try {
			// Tìm nhân viên theo ID, ném ngoại lệ nếu không tìm thấy
			EmployeeEntity employee = employeeRepository.findById(request.getId())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

			// Cập nhật thông tin cơ bản
			employee.setFullName(request.getFullName());
			employee.setAddress(request.getAddress());
			employee.setPhoneNumber(request.getPhoneNumber());
			// Không có thuộc tính ngày tháng nào cần xử lý ở đây.

			// Cập nhật chức vụ và lương nếu có
			if (employee.getPosition() != null) {
				employee.getPosition().setPositionName(request.getPosition());
				employee.getPosition().setSalary(request.getSalary());
			}

			// Cập nhật thông tin tài khoản nếu có
			if (employee.getAccount() != null) {
				employee.getAccount().setUsername(request.getUsername());
				if (request.getPassword() != null && !request.getPassword().isBlank()) {
					employee.getAccount().setPassword(passwordEncoder.encode(request.getPassword()));
				}
				if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
					employee.getAccount().setImageUrl(request.getImageUrl());
				}
			}

			// Lưu thông tin nhân viên đã cập nhật
			employeeRepository.save(employee);
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi cập nhật nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Cập nhật thông tin hồ sơ cá nhân dựa trên yêu cầu từ trang cá nhân. Không cho
	 * phép cập nhật tên đăng nhập và mật khẩu.
	 * 
	 * @param request Đối tượng ProfileUpdateRequest chứa thông tin cần cập nhật.
	 */
	@Override
	public void updateProfile(ProfileUpdateRequest request) {
		try {
			// Tìm nhân viên theo ID, ném ngoại lệ nếu không tìm thấy
			EmployeeEntity employee = employeeRepository.findById(request.getId())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

			// Cập nhật thông tin cơ bản
			employee.setFullName(request.getFullName());
			employee.setAddress(request.getAddress());
			employee.setPhoneNumber(request.getPhoneNumber());
			// Không có thuộc tính ngày tháng nào cần xử lý ở đây.

			// Cập nhật chức vụ và lương nếu có
			if (employee.getPosition() != null) {
				employee.getPosition().setPositionName(request.getPosition());
				employee.getPosition().setSalary(request.getSalary());
			}

			// Lưu thông tin nhân viên đã cập nhật
			employeeRepository.save(employee);
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi cập nhật thông tin cá nhân: " + e.getMessage());
		}
	}

	/**
	 * Tìm nhân viên theo tên đăng nhập.
	 * 
	 * @param username Tên đăng nhập của nhân viên.
	 * @return Đối tượng EmployeeEntity tìm thấy.
	 */
	@Override
	public EmployeeEntity findByUsername(String username) {
		// Tìm nhân viên theo username, ném ngoại lệ nếu không tìm thấy
		return employeeRepository.findByAccount_Username(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));
	}

	/**
	 * Lấy thông tin hồ sơ nhân viên dưới dạng DTO dựa trên tên đăng nhập.
	 * 
	 * @param username Tên đăng nhập của nhân viên.
	 * @return Đối tượng EmployeeProfileDTO chứa thông tin hồ sơ.
	 */
	@Override
	public EmployeeProfileDTO getProfileDTO(String username) {
		try {
			// Tìm nhân viên theo username
			EmployeeEntity employee = findByUsername(username);
			// Tạo và trả về DTO với thông tin nhân viên
			return new EmployeeProfileDTO(employee.getId(), employee.getFullName(),
					employee.getPosition() != null ? employee.getPosition().getPositionName() : null,
					employee.getAddress(), employee.getPhoneNumber(),
					employee.getPosition() != null ? employee.getPosition().getSalary() : null,
					employee.getAccount() != null ? employee.getAccount().getUsername() : null,
					employee.getAccount() != null ? employee.getAccount().getPassword() : null,
					employee.getAccount() != null ? employee.getAccount().getImageUrl() : null);
			// Không có thuộc tính ngày tháng nào cần xử lý ở đây.
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi lấy thông tin DTO nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Lấy thông tin hồ sơ nhân viên dưới dạng Response dựa trên tên đăng nhập.
	 * 
	 * @param username Tên đăng nhập của nhân viên.
	 * @return Đối tượng EmployeeProfileResponse chứa thông tin hồ sơ.
	 */
	@Override
	public EmployeeProfileResponse getProfileByUsername(String username) {
		// Chuyển đổi từ entity sang response
		return convertToResponse(findByUsername(username));
	}

	/**
	 * Lấy thông tin nhân viên theo ID.
	 * 
	 * @param id ID của nhân viên cần tìm.
	 * @return Đối tượng EmployeeEntity tìm thấy.
	 */
	@Override
	public EmployeeEntity getById(Integer id) {
		// Tìm nhân viên theo ID, ném ngoại lệ nếu không tìm thấy
		return employeeRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + id));
	}

	/**
	 * Tạo mới một nhân viên từ yêu cầu.
	 * 
	 * @param request Đối tượng EmployeeProfileRequest chứa thông tin để tạo nhân
	 *                viên.
	 */
	@Override
	public void create(EmployeeProfileRequest request) {
		try {
			// Tạo mới nhân viên
			EmployeeEntity employee = new EmployeeEntity();
			employee.setFullName(request.getFullName());
			employee.setAddress(request.getAddress());
			employee.setPhoneNumber(request.getPhoneNumber());
			employee.setIsDeleted(false);
			// Không có thuộc tính ngày tháng nào cần xử lý ở đây.

			// Tạo và gán chức vụ
			PositionEntity position = new PositionEntity();
			position.setPositionName(request.getPosition());
			position.setSalary(request.getSalary());
			position.setIsDeleted(false);
			employee.setPosition(position);

			// Tạo và gán tài khoản
			AccountEntity account = new AccountEntity();
			account.setUsername(request.getUsername());
			account.setPassword(passwordEncoder.encode(request.getPassword()));
			account.setImageUrl(request.getImageUrl());
			account.setPermission("EMPLOYEE");
			account.setIsDeleted(false);
			employee.setAccount(account);

			// Lưu nhân viên vào cơ sở dữ liệu
			employeeRepository.save(employee);
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi tạo nhân viên mới: " + e.getMessage());
		}
	}

	/**
	 * Xóa nhân viên (đánh dấu xóa mềm) theo ID.
	 * 
	 * @param id ID của nhân viên cần xóa.
	 */
	@Override
	public void deleteById(Integer id) {
		try {
			// Tìm nhân viên theo ID, ném ngoại lệ nếu không tìm thấy
			EmployeeEntity employee = employeeRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
			// Đánh dấu xóa mềm
			employee.setIsDeleted(true);
			// Lưu thay đổi
			employeeRepository.save(employee);
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi xóa nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Lấy danh sách tất cả nhân viên đang hoạt động.
	 * 
	 * @return Một List các EmployeeProfileResponse của các nhân viên hoạt động.
	 */
	@Override
	public List<EmployeeProfileResponse> getAllActive() {
		try {
			// Lấy danh sách nhân viên chưa bị xóa và chuyển thành response
			return employeeRepository.findAllByIsDeletedFalse().stream().map(this::convertToResponse)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi lấy danh sách nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Tìm kiếm nhân viên theo từ khóa (tên, chức vụ hoặc lương).
	 * 
	 * @param keyword Từ khóa dùng để tìm kiếm (ví dụ: theo tên, số điện thoại,
	 *                v.v.).
	 * @return Một List các EmployeeProfileResponse khớp với từ khóa tìm kiếm.
	 */
	@Override
	public List<EmployeeProfileResponse> search(String keyword) {
		try {
			List<EmployeeEntity> results;
			try {
				// Thử tìm kiếm theo lương nếu từ khóa là số
				Double salary = Double.parseDouble(keyword);
				results = employeeRepository.findByIsDeletedFalseAndPosition_Salary(salary);
			} catch (NumberFormatException e) {
				// Tìm kiếm theo tên hoặc chức vụ nếu từ khóa không phải số
				results = employeeRepository
						.findByIsDeletedFalseAndFullNameContainingIgnoreCaseOrPosition_PositionNameContainingIgnoreCase(
								keyword, keyword);
			}
			// Chuyển đổi kết quả sang response
			return results.stream().map(this::convertToResponse).collect(Collectors.toList());
		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi tìm kiếm nhân viên: " + e.getMessage());
		}
	}

	/**
	 * Chuyển đổi từ EmployeeEntity sang EmployeeProfileResponse.
	 * 
	 * @param employee Thực thể EmployeeEntity cần chuyển đổi.
	 * @return Đối tượng EmployeeProfileResponse đã được chuyển đổi.
	 */
	@Override
	public EmployeeProfileResponse convertToResponse(EmployeeEntity employee) {
		// Tạo đối tượng response
		EmployeeProfileResponse res = new EmployeeProfileResponse();
		try {
			res.setId(employee.getId());
			res.setFullName(employee.getFullName());
			res.setAddress(employee.getAddress());
			res.setPhoneNumber(employee.getPhoneNumber());
			// Không có thuộc tính ngày tháng nào cần xử lý ở đây.

			// Gán thông tin chức vụ nếu có
			if (employee.getPosition() != null) {
				res.setPosition(employee.getPosition().getPositionName());
				res.setSalary(employee.getPosition().getSalary());
			}

			// Gán thông tin tài khoản nếu có
			if (employee.getAccount() != null) {
				res.setUsername(employee.getAccount().getUsername());
				res.setPassword(employee.getAccount().getPassword());
				res.setImageUrl(employee.getAccount().getImageUrl());
			}

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi chuyển đổi thông tin nhân viên: " + e.getMessage());
		}

		return res;
	}
}