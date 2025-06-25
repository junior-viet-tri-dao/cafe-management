package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.controller.response.EmployeeProfileResponse;
import com.viettridao.cafe.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các yêu cầu liên quan đến quản lý thông tin nhân viên. Cung
 * cấp các chức năng như liệt kê, thêm, sửa, xóa và tìm kiếm nhân viên.
 */
@Controller // Đánh dấu lớp này là một Spring MVC Controller
@RequestMapping("/employee") // Đặt base path cho tất cả các endpoint trong controller này
@RequiredArgsConstructor // Tự động tạo constructor để inject các dependency final (EmployeeService)
public class EmployeeController {

	// Inject EmployeeService để sử dụng các phương thức xử lý nghiệp vụ liên quan
	// đến nhân viên
	private final EmployeeService employeeService;

	/**
	 * Xử lý yêu cầu HTTP GET đến "/employee" hoặc "/employee/". Phương thức này
	 * dùng để hiển thị danh sách tất cả nhân viên đang hoạt động.
	 *
	 * @param model Đối tượng Model để truyền danh sách nhân viên đến view.
	 * @return Tên view của trang danh sách nhân viên ("employee/list").
	 */
	@GetMapping // Xử lý yêu cầu GET cho base path "/employee"
	public String list(Model model) {
		// Thêm danh sách tất cả nhân viên đang hoạt động vào model
		model.addAttribute("employees", employeeService.getAllActive());
		return "employee/list"; // Trả về view để hiển thị danh sách
	}

	/**
	 * Xử lý yêu cầu HTTP GET đến "/employee/add". Phương thức này dùng để hiển thị
	 * form thêm nhân viên mới.
	 *
	 * @param model Đối tượng Model để truyền một đối tượng EmployeeProfileRequest
	 *              rỗng đến view, để binding dữ liệu từ form.
	 * @return Tên view của trang thêm nhân viên ("employee/add").
	 */
	@GetMapping("/add") // Xử lý yêu cầu GET đến "/employee/add"
	public String addForm(Model model) {
		// Khởi tạo một đối tượng request rỗng để dùng cho form thêm mới
		model.addAttribute("request", new EmployeeProfileRequest());
		return "employee/add"; // Trả về view để hiển thị form thêm
	}

	/**
	 * Xử lý yêu cầu HTTP POST đến "/employee/add". Phương thức này nhận dữ liệu từ
	 * form thêm nhân viên và thực hiện thêm mới nhân viên vào hệ thống.
	 *
	 * @param request            Đối tượng EmployeeProfileRequest chứa thông tin
	 *                           nhân viên từ form, được @Valid kiểm tra ràng buộc.
	 * @param result             Đối tượng BindingResult chứa kết quả của quá trình
	 *                           validation.
	 * @param model              Đối tượng Model để truyền dữ liệu hoặc thông báo
	 *                           lỗi nếu có.
	 * @param redirectAttributes Đối tượng dùng để thêm thuộc tính flash cho
	 *                           redirect (thông báo thành công/thất bại).
	 * @return Chuyển hướng đến trang danh sách nhân viên nếu thành công, hoặc quay
	 *         lại form thêm nếu có lỗi validation hoặc lỗi nghiệp vụ.
	 */
	@PostMapping("/add") // Xử lý yêu cầu POST đến "/employee/add"
	public String addSubmit(@Valid @ModelAttribute("request") EmployeeProfileRequest request, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		// Kiểm tra nếu có lỗi validation từ @Valid
		if (result.hasErrors()) {
			return "employee/add"; // Quay lại form thêm với các lỗi hiển thị
		}

		try {
			// Gọi dịch vụ để tạo nhân viên mới
			employeeService.create(request);
			// Thêm thông báo thành công vào flash attribute để hiển thị sau redirect
			redirectAttributes.addFlashAttribute("success", "Thêm nhân viên thành công!");
			return "redirect:/employee"; // Chuyển hướng về trang danh sách nhân viên
		} catch (Exception e) {
			// Nếu có lỗi nghiệp vụ (ví dụ: trùng username), thêm thông báo lỗi vào model
			model.addAttribute("error", "Thêm thất bại: " + e.getMessage());
			return "employee/add"; // Quay lại form thêm để người dùng chỉnh sửa
		}
	}

	/**
	 * Xử lý yêu cầu HTTP GET đến "/employee/edit/{id}". Phương thức này dùng để
	 * hiển thị form chỉnh sửa thông tin nhân viên dựa trên ID.
	 *
	 * @param id    Mã định danh của nhân viên cần chỉnh sửa, lấy từ URL path.
	 * @param model Đối tượng Model để truyền thông tin nhân viên hiện tại đến view,
	 *              để form có thể hiển thị dữ liệu có sẵn.
	 * @return Tên view của trang chỉnh sửa nhân viên ("employee/edit").
	 */
	@GetMapping("/edit/{id}") // Xử lý yêu cầu GET đến "/employee/edit/{id}"
	public String editForm(@PathVariable Integer id, Model model) {
		// Lấy thông tin nhân viên bằng ID và chuyển đổi sang đối tượng Response
		EmployeeProfileResponse employee = employeeService.convertToResponse(employeeService.getById(id));
		// Tạo một đối tượng Request mới và gán dữ liệu từ Response vào Request
		// để nó có thể được binding với form chỉnh sửa
		EmployeeProfileRequest request = new EmployeeProfileRequest();

		request.setId(employee.getId());
		request.setFullName(employee.getFullName());
		request.setAddress(employee.getAddress());
		request.setPhoneNumber(employee.getPhoneNumber());
		request.setPosition(employee.getPosition());
		request.setSalary(employee.getSalary());
		request.setImageUrl(employee.getImageUrl());
		request.setUsername(employee.getUsername());
		request.setPassword(employee.getPassword()); // Mật khẩu có thể cần xử lý đặc biệt

		// Thêm đối tượng request đã điền dữ liệu vào model
		model.addAttribute("request", request);
		return "employee/edit"; // Trả về view để hiển thị form chỉnh sửa
	}

	/**
	 * Xử lý yêu cầu HTTP POST đến "/employee/edit". Phương thức này nhận dữ liệu từ
	 * form chỉnh sửa nhân viên và thực hiện cập nhật thông tin nhân viên.
	 *
	 * @param request            Đối tượng EmployeeProfileRequest chứa thông tin
	 *                           nhân viên đã chỉnh sửa, được @Valid kiểm tra ràng
	 *                           buộc.
	 * @param result             Đối tượng BindingResult chứa kết quả của quá trình
	 *                           validation.
	 * @param model              Đối tượng Model để truyền dữ liệu hoặc thông báo
	 *                           lỗi nếu có.
	 * @param redirectAttributes Đối tượng dùng để thêm thuộc tính flash cho
	 *                           redirect.
	 * @return Chuyển hướng đến trang danh sách nhân viên nếu thành công, hoặc quay
	 *         lại form chỉnh sửa nếu có lỗi validation hoặc lỗi nghiệp vụ.
	 */
	@PostMapping("/edit") // Xử lý yêu cầu POST đến "/employee/edit"
	public String editSubmit(@Valid @ModelAttribute("request") EmployeeProfileRequest request, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		// Kiểm tra nếu có lỗi validation từ @Valid
		if (result.hasErrors()) {
			return "employee/edit"; // Quay lại form chỉnh sửa với các lỗi hiển thị
		}

		try {
			// Gọi dịch vụ để cập nhật hồ sơ nhân viên
			employeeService.updateProfile(request);
			// Thêm thông báo thành công vào flash attribute
			redirectAttributes.addFlashAttribute("success", "Cập nhật nhân viên thành công!");
			return "redirect:/employee"; // Chuyển hướng về trang danh sách nhân viên
		} catch (Exception e) {
			// Nếu có lỗi nghiệp vụ, thêm thông báo lỗi vào model
			model.addAttribute("error", "Cập nhật thất bại: " + e.getMessage());
			return "employee/edit"; // Quay lại form chỉnh sửa
		}
	}

	/**
	 * Xử lý yêu cầu HTTP POST đến "/employee/delete/{id}". Phương thức này thực
	 * hiện xóa (logic delete) một nhân viên dựa trên ID.
	 *
	 * @param id                 Mã định danh của nhân viên cần xóa, lấy từ URL
	 *                           path.
	 * @param redirectAttributes Đối tượng dùng để thêm thuộc tính flash cho
	 *                           redirect.
	 * @return Chuyển hướng đến trang danh sách nhân viên sau khi xử lý xóa.
	 */
	@PostMapping("/delete/{id}") // Xử lý yêu cầu POST đến "/employee/delete/{id}"
	public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			// Gọi dịch vụ để xóa nhân viên (thực hiện xóa mềm)
			employeeService.deleteById(id);
			// Thêm thông báo thành công vào flash attribute
			redirectAttributes.addFlashAttribute("success", "Xóa nhân viên thành công!");
		} catch (Exception e) {
			// Nếu có lỗi trong quá trình xóa, thêm thông báo lỗi vào flash attribute
			redirectAttributes.addFlashAttribute("error", "Xóa thất bại: " + e.getMessage());
		}
		return "redirect:/employee"; // Luôn chuyển hướng về trang danh sách nhân viên
	}

	/**
	 * Xử lý yêu cầu HTTP GET đến "/employee/search". Phương thức này tìm kiếm nhân
	 * viên dựa trên từ khóa và hiển thị kết quả.
	 *
	 * @param keyword Từ khóa tìm kiếm, lấy từ request parameter "keyword".
	 * @param model   Đối tượng Model để truyền danh sách nhân viên tìm được và từ
	 *                khóa đến view.
	 * @return Tên view của trang danh sách nhân viên ("employee/list") để hiển thị
	 *         kết quả tìm kiếm.
	 */
	@GetMapping("/search") // Xử lý yêu cầu GET đến "/employee/search"
	public String search(@RequestParam("keyword") String keyword, Model model) {
		// Gọi dịch vụ để tìm kiếm nhân viên theo từ khóa
		model.addAttribute("employees", employeeService.search(keyword));
		// Giữ lại từ khóa tìm kiếm trong model để hiển thị lại trên giao diện
		model.addAttribute("keyword", keyword);
		return "employee/list"; // Trả về view để hiển thị kết quả tìm kiếm
	}
}