package com.viettridao.cafe.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Lớp xử lý ngoại lệ toàn cục cho ứng dụng Spring MVC. Được đánh dấu
 * bằng @ControllerAdvice để bắt các ngoại lệ từ tất cả các controller.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	// Khởi tạo logger để ghi lại thông tin và lỗi trong quá trình ứng dụng chạy.
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Xử lý các lỗi validation xảy ra khi dữ liệu form không hợp lệ (ví dụ: khi sử
	 * dụng @Valid). Phương thức này thu thập tất cả các lỗi trường và hiển thị
	 * chúng trên giao diện người dùng.
	 *
	 * @param ex      Ngoại lệ MethodArgumentNotValidException chứa thông tin chi
	 *                tiết về lỗi validation.
	 * @param model   Đối tượng Model để thêm các thuộc tính cho view.
	 * @param request Đối tượng HttpServletRequest để lấy URI của yêu cầu hiện tại.
	 * @return Tên view để hiển thị lại form với các thông báo lỗi hoặc một trang
	 *         lỗi chung.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidation(MethodArgumentNotValidException ex, Model model, HttpServletRequest request) {
		// Tạo một HashMap để lưu trữ tên trường và thông báo lỗi tương ứng.
		Map<String, String> errors = new HashMap<>();
		// Lặp qua tất cả các lỗi trường (FieldError) và thêm vào map.
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		// Thêm map lỗi vào Model để hiển thị trên trang HTML (ví dụ: thông qua
		// Thymeleaf).
		model.addAttribute("errors", errors);

		// Lấy URI của request hiện tại để quyết định trang nào cần trả về cụ thể.
		String uri = request.getRequestURI();

		// Kiểm tra URI để chuyển hướng người dùng trở lại form chính xác với lỗi hiển
		// thị.
		if (uri.contains("/employee/add"))
			return "employee/add"; // Trả về trang thêm nhân viên.
		if (uri.contains("/employee/edit"))
			return "employee/edit"; // Trả về trang chỉnh sửa nhân viên.
		if (uri.contains("/equipment/add"))
			return "equipment/add"; // Trả về trang thêm thiết bị.
		if (uri.contains("/equipment/edit"))
			return "equipment/edit"; // Trả về trang chỉnh sửa thiết bị.

		// Ghi log cảnh báo về lỗi validation, bao gồm URI của request.
		logger.warn("❌ Validation lỗi tại {}", uri);
		// Mặc định, nếu không khớp với URI cụ thể nào, trả về trang lỗi chung.
		return "error/general";
	}

	/**
	 * Xử lý ngoại lệ EntityNotFoundException, xảy ra khi một thực thể (dữ liệu)
	 * không tìm thấy trong cơ sở dữ liệu (ví dụ: khi tìm kiếm bằng ID không tồn
	 * tại).
	 *
	 * @param ex                 Ngoại lệ EntityNotFoundException chứa thông báo
	 *                           lỗi.
	 * @param request            Đối tượng HttpServletRequest để lấy URI của yêu
	 *                           cầu.
	 * @param model              Đối tượng Model để truyền thông báo lỗi đến view.
	 * @param redirectAttributes Đối tượng RedirectAttributes để thêm thông báo
	 *                           flash (flash attributes) khi chuyển hướng.
	 * @return Chuyển hướng đến trang đăng nhập nếu lỗi xảy ra ở đó, hoặc hiển thị
	 *         trang lỗi chung.
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	public String handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		// Lấy URI của request hiện tại.
		String uri = request.getRequestURI();
		// Ghi log cảnh báo khi không tìm thấy dữ liệu.
		logger.warn("⚠ Không tìm thấy dữ liệu tại {}", uri);

		// Nếu lỗi xảy ra trong quá trình xử lý liên quan đến "/login", chuyển hướng về
		// trang login với thông báo lỗi.
		if (uri.contains("/login")) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/login";
		}

		// Thêm thông điệp lỗi vào Model để hiển thị trên trang lỗi.
		model.addAttribute("errorMessage", ex.getMessage());
		// Trả về trang lỗi chung để thông báo cho người dùng.
		return "error/general";
	}

	/**
	 * Xử lý ngoại lệ NoHandlerFoundException, xảy ra khi không tìm thấy handler
	 * (controller method) cho một yêu cầu HTTP, thường dẫn đến lỗi HTTP 404 (Not
	 * Found).
	 *
	 * @param ex      Ngoại lệ NoHandlerFoundException.
	 * @param request Đối tượng HttpServletRequest để lấy URI của yêu cầu.
	 * @param model   Đối tượng Model để truyền thông báo lỗi đến view.
	 * @return Tên view của trang lỗi 404 tùy chỉnh.
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handle404(NoHandlerFoundException ex, HttpServletRequest request, Model model) {
		// Ghi log cảnh báo khi có lỗi 404 Not Found, bao gồm URI của request.
		logger.warn("📛 404 NOT FOUND tại {}", request.getRequestURI());
		// Thêm thông điệp lỗi vào Model để thông báo cho người dùng.
		model.addAttribute("errorMessage", "Trang bạn yêu cầu không tồn tại.");
		// Trả về trang lỗi 404 tùy chỉnh.
		return "error/404";
	}

	/**
	 * Xử lý các ngoại lệ RuntimeException, là lớp cơ sở cho hầu hết các lỗi không
	 * được kiểm tra (unchecked exceptions). Đây thường là các lỗi logic hoặc lỗi hệ
	 * thống không lường trước được.
	 *
	 * @param ex                 Ngoại lệ RuntimeException chứa thông báo lỗi.
	 * @param request            Đối tượng HttpServletRequest để lấy URI của yêu
	 *                           cầu.
	 * @param model              Đối tượng Model để truyền thông báo lỗi đến view.
	 * @param redirectAttributes Đối tượng RedirectAttributes để thêm thông báo
	 *                           flash khi chuyển hướng.
	 * @return Chuyển hướng đến trang đăng nhập nếu lỗi xảy ra ở đó, hoặc hiển thị
	 *         trang lỗi 500.
	 */
	@ExceptionHandler(RuntimeException.class)
	public String handleRuntime(RuntimeException ex, HttpServletRequest request, Model model,
			RedirectAttributes redirectAttributes) {
		// Lấy URI của request hiện tại.
		String uri = request.getRequestURI();
		// Ghi log lỗi hệ thống ở mức ERROR, bao gồm URI và thông điệp lỗi chi tiết.
		logger.error("🔥 Lỗi hệ thống tại {}: {}", uri, ex.getMessage());

		// Nếu lỗi xảy ra trong quá trình xử lý liên quan đến "/login", chuyển hướng về
		// trang login với thông báo lỗi.
		if (uri.contains("/login")) {
			redirectAttributes.addFlashAttribute("error", ex.getMessage());
			return "redirect:/login";
		}

		// Thêm thông điệp lỗi vào Model để hiển thị.
		model.addAttribute("errorMessage", "Lỗi hệ thống: " + ex.getMessage());
		// Trả về trang lỗi 500 (Internal Server Error).
		return "error/500";
	}

	/**
	 * Xử lý tất cả các loại ngoại lệ không xác định (Exception.class) mà không được
	 * xử lý bởi các phương thức @ExceptionHandler cụ thể ở trên. Đây là một phương
	 * thức "bắt lỗi tổng quát" để đảm bảo mọi lỗi đều được ghi lại và hiển thị.
	 *
	 * @param ex      Ngoại lệ Exception (lớp cơ sở của tất cả các ngoại lệ).
	 * @param request Đối tượng HttpServletRequest để lấy URI của yêu cầu.
	 * @param model   Đối tượng Model để truyền thông báo lỗi đến view.
	 * @return Tên view của trang lỗi 500.
	 */
	@ExceptionHandler(Exception.class)
	public String handleOther(Exception ex, HttpServletRequest request, Model model) {
		// Ghi log lỗi không mong muốn ở mức ERROR, bao gồm URI và thông điệp lỗi chi
		// tiết.
		logger.error("❗ Lỗi không mong muốn tại {}: {}", request.getRequestURI(), ex.getMessage());
		// Thêm thông điệp lỗi vào Model để hiển thị.
		model.addAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn: " + ex.getMessage());
		// Trả về trang lỗi 500 (Internal Server Error).
		return "error/500";
	}
}