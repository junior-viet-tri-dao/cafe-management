package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.EquipmentRequest;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.service.EquipmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý các yêu cầu liên quan đến quản lý thiết bị. Bao gồm các chức
 * năng như hiển thị danh sách, thêm, sửa, xóa thiết bị.
 */
@Controller // Đánh dấu đây là một Spring MVC Controller
@RequestMapping("/equipment") // Đặt base path cho tất cả các endpoint trong controller này là "/equipment"
@RequiredArgsConstructor // Tự động tạo constructor để inject các dependency final (EquipmentService)
public class EquipmentController {

	// Inject EquipmentService để sử dụng các phương thức xử lý nghiệp vụ liên quan
	// đến thiết bị
	private final EquipmentService equipmentService;

	/**
	 * Chuyển hướng yêu cầu GET từ "/equipment/list" về "/equipment". Điều này đảm
	 * bảo rằng cả hai URL đều dẫn đến trang danh sách thiết bị chính.
	 *
	 * @return Chuỗi "redirect:/equipment" để chuyển hướng.
	 */
	@GetMapping("/list") // Xử lý yêu cầu GET đến "/equipment/list"
	public String redirectToList() {
		return "redirect:/equipment"; // Chuyển hướng trình duyệt đến "/equipment"
	}

	/**
	 * Xử lý yêu cầu HTTP GET đến "/equipment" hoặc "/equipment/". Phương thức này
	 * dùng để hiển thị danh sách tất cả thiết bị đang hoạt động.
	 *
	 * @param model Đối tượng Model để truyền danh sách thiết bị đến view.
	 * @return Tên view của trang danh sách thiết bị ("equipment/list").
	 */
	@GetMapping // Xử lý yêu cầu GET cho base path "/equipment"
	public String list(Model model) {
		// Thêm danh sách tất cả thiết bị đang hoạt động (dưới dạng Response) vào model
		model.addAttribute("equipments", equipmentService.getAllActiveAsResponse());
		return "equipment/list"; // Trả về view để hiển thị danh sách thiết bị
	}

	/**
	 * Xử lý yêu cầu HTTP GET đến "/equipment/add". Phương thức này dùng để hiển thị
	 * form thêm thiết bị mới.
	 *
	 * @param model Đối tượng Model để truyền một đối tượng EquipmentRequest rỗng
	 *              đến view, để binding dữ liệu từ form.
	 * @return Tên view của trang thêm thiết bị ("equipment/add").
	 */
	@GetMapping("/add") // Xử lý yêu cầu GET đến "/equipment/add"
	public String addForm(Model model) {
		// Khởi tạo một đối tượng request rỗng để dùng cho form thêm mới
		model.addAttribute("request", new EquipmentRequest());
		return "equipment/add"; // Trả về view để hiển thị form thêm thiết bị
	}

	/**
	 * Xử lý yêu cầu HTTP POST đến "/equipment/add". Phương thức này nhận dữ liệu từ
	 * form thêm thiết bị và thực hiện thêm mới thiết bị vào hệ thống.
	 *
	 * @param request            Đối tượng EquipmentRequest chứa thông tin thiết bị
	 *                           từ form, được @Valid kiểm tra ràng buộc.
	 * @param result             Đối tượng BindingResult chứa kết quả của quá trình
	 *                           validation.
	 * @param model              Đối tượng Model để truyền dữ liệu hoặc thông báo
	 *                           lỗi nếu có.
	 * @param redirectAttributes Đối tượng dùng để thêm thuộc tính flash cho
	 *                           redirect (thông báo thành công/thất bại).
	 * @return Chuyển hướng đến trang danh sách thiết bị nếu thành công, hoặc quay
	 *         lại form thêm nếu có lỗi validation hoặc lỗi nghiệp vụ.
	 */
	@PostMapping("/add") // Xử lý yêu cầu POST đến "/equipment/add"
	public String addSubmit(@Valid @ModelAttribute("request") EquipmentRequest request, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		// Kiểm tra nếu có lỗi validation từ @Valid
		if (result.hasErrors()) {
			return "equipment/add"; // Quay lại form thêm với các lỗi hiển thị
		}

		try {
			// Gọi dịch vụ để tạo thiết bị mới
			equipmentService.create(request);
			// Thêm thông báo thành công vào flash attribute để hiển thị sau redirect
			redirectAttributes.addFlashAttribute("success", "Thêm thiết bị thành công!");
			return "redirect:/equipment"; // Chuyển hướng về trang danh sách thiết bị
		} catch (Exception e) {
			// Nếu có lỗi nghiệp vụ, thêm thông báo lỗi vào model
			model.addAttribute("error", "Thêm thất bại: " + e.getMessage());
			return "equipment/add"; // Quay lại form thêm để người dùng chỉnh sửa
		}
	}

	/**
	 * Xử lý yêu cầu HTTP GET đến "/equipment/edit/{id}". Phương thức này dùng để
	 * hiển thị form chỉnh sửa thông tin thiết bị dựa trên ID.
	 *
	 * @param id    Mã định danh của thiết bị cần chỉnh sửa, lấy từ URL path.
	 * @param model Đối tượng Model để truyền thông tin thiết bị hiện tại đến view,
	 *              để form có thể hiển thị dữ liệu có sẵn.
	 * @return Tên view của trang chỉnh sửa thiết bị ("equipment/edit").
	 */
	@GetMapping("/edit/{id}") // Xử lý yêu cầu GET đến "/equipment/edit/{id}"
	public String editForm(@PathVariable Integer id, Model model) {
		// Lấy thông tin thiết bị bằng ID và chuyển đổi sang đối tượng Request
		EquipmentRequest request = convertToRequest(equipmentService.getById(id));
		// Thêm đối tượng request đã điền dữ liệu vào model
		model.addAttribute("request", request);
		return "equipment/edit"; // Trả về view để hiển thị form chỉnh sửa
	}

	/**
	 * Xử lý yêu cầu HTTP POST đến "/equipment/edit". Phương thức này nhận dữ liệu
	 * từ form chỉnh sửa thiết bị và thực hiện cập nhật thông tin thiết bị.
	 *
	 * @param request            Đối tượng EquipmentRequest chứa thông tin thiết bị
	 *                           đã chỉnh sửa, được @Valid kiểm tra ràng buộc.
	 * @param result             Đối tượng BindingResult chứa kết quả của quá trình
	 *                           validation.
	 * @param model              Đối tượng Model để truyền dữ liệu hoặc thông báo
	 *                           lỗi nếu có.
	 * @param redirectAttributes Đối tượng dùng để thêm thuộc tính flash cho
	 *                           redirect.
	 * @return Chuyển hướng đến trang danh sách thiết bị nếu thành công, hoặc quay
	 *         lại form chỉnh sửa nếu có lỗi validation hoặc lỗi nghiệp vụ.
	 */
	@PostMapping("/edit") // Xử lý yêu cầu POST đến "/equipment/edit"
	public String editSubmit(@Valid @ModelAttribute("request") EquipmentRequest request, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		// Kiểm tra nếu có lỗi validation từ @Valid
		if (result.hasErrors()) {
			return "equipment/edit"; // Quay lại form chỉnh sửa với các lỗi hiển thị
		}

		try {
			// Gọi dịch vụ để cập nhật thiết bị
			equipmentService.update(request);
			// Thêm thông báo thành công vào flash attribute
			redirectAttributes.addFlashAttribute("success", "Cập nhật thiết bị thành công!");
			return "redirect:/equipment"; // Chuyển hướng về trang danh sách thiết bị
		} catch (Exception e) {
			// Nếu có lỗi nghiệp vụ, thêm thông báo lỗi vào model
			model.addAttribute("error", "Cập nhật thất bại: " + e.getMessage());
			return "equipment/edit"; // Quay lại form chỉnh sửa
		}
	}

	/**
	 * Xử lý yêu cầu HTTP POST đến "/equipment/delete/{id}". Phương thức này thực
	 * hiện xóa (logic delete) một thiết bị dựa trên ID.
	 *
	 * @param id                 Mã định danh của thiết bị cần xóa, lấy từ URL path.
	 * @param redirectAttributes Đối tượng dùng để thêm thuộc tính flash cho
	 *                           redirect.
	 * @return Chuyển hướng đến trang danh sách thiết bị sau khi xử lý xóa.
	 */
	@PostMapping("/delete/{id}") // Xử lý yêu cầu POST đến "/equipment/delete/{id}"
	public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			// Gọi dịch vụ để xóa thiết bị (thực hiện xóa mềm)
			equipmentService.delete(id);
			// Thêm thông báo thành công vào flash attribute
			redirectAttributes.addFlashAttribute("success", "Xóa thiết bị thành công!");
		} catch (Exception e) {
			// Nếu có lỗi trong quá trình xóa, thêm thông báo lỗi vào flash attribute
			redirectAttributes.addFlashAttribute("error", "Xóa thất bại: " + e.getMessage());
		}
		return "redirect:/equipment"; // Luôn chuyển hướng về trang danh sách thiết bị
	}

	/**
	 * Phương thức trợ giúp để chuyển đổi một EquipmentEntity thành một
	 * EquipmentRequest. Thường được sử dụng khi hiển thị dữ liệu entity vào một
	 * form request để chỉnh sửa.
	 *
	 * @param e Đối tượng EquipmentEntity cần chuyển đổi.
	 * @return EquipmentRequest chứa dữ liệu đã được gán từ entity.
	 */
	private EquipmentRequest convertToRequest(EquipmentEntity e) {
		EquipmentRequest r = new EquipmentRequest();
		r.setId(e.getId());
		r.setEquipmentName(e.getEquipmentName());
		r.setPurchaseDate(e.getPurchaseDate()); // Gán ngày mua thiết bị
		r.setQuantity(e.getQuantity());
		r.setPurchasePrice(e.getPurchasePrice());
		r.setNotes(e.getNotes());
		return r;
	}
}