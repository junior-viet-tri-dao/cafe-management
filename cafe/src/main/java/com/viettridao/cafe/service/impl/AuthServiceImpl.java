package com.viettridao.cafe.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.viettridao.cafe.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/**
 * Lớp triển khai dịch vụ xác thực người dùng (AuthService). Sử dụng Spring
 * Security để xử lý đăng nhập và lưu thông tin xác thực vào session.
 */
@RequiredArgsConstructor // Tự động tạo constructor để inject các dependency final
@Service // Đánh dấu đây là một lớp dịch vụ trong tầng business logic
public class AuthServiceImpl implements AuthService {
	// Quản lý xác thực của Spring Security, chịu trách nhiệm xác minh thông tin
	// đăng nhập
	private final AuthenticationManager authenticationManager;
	// Đối tượng HttpServletRequest để truy cập session hiện tại
	private final HttpServletRequest request;

	/**
	 * Phương thức xử lý đăng nhập người dùng. Thực hiện xác thực thông tin đăng
	 * nhập và thiết lập ngữ cảnh bảo mật cho phiên làm việc.
	 *
	 * @param username Tên đăng nhập của người dùng.
	 * @param password Mật khẩu của người dùng.
	 * @return **true** nếu đăng nhập thành công.
	 * @throws RuntimeException nếu thông tin đăng nhập không hợp lệ (thiếu, sai
	 *                          username/password).
	 */
	@Override
	public boolean login(String username, String password) {
		// Kiểm tra xem tên đăng nhập và mật khẩu có rỗng hoặc chỉ chứa khoảng trắng hay
		// không
		if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
			// Ném ngoại lệ nếu thông tin không đầy đủ
			throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
		}

		try {
			// Bước 1: Tạo đối tượng xác thực (Authentication Token) với tên đăng nhập và
			// mật khẩu
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			// Bước 2: Yêu cầu AuthenticationManager xác thực người dùng
			Authentication authentication = authenticationManager.authenticate(authToken);

			// Bước 3: Lưu thông tin xác thực đã thành công vào SecurityContextHolder
			// Điều này cho phép Spring Security biết người dùng hiện tại đã được xác thực
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// Bước 4: Tạo hoặc lấy session hiện tại
			HttpSession session = request.getSession(true);
			// Bước 5: Lưu SecurityContext vào HttpSession.
			// Điều này rất quan trọng để duy trì trạng thái đăng nhập của người dùng
			// trên các request tiếp theo trong cùng một session.
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());

			// Trả về true nếu quá trình xác thực và lưu session thành công
			return true;
		} catch (AuthenticationException e) {
			// Bắt các ngoại lệ liên quan đến xác thực (ví dụ: sai tên đăng nhập, sai mật
			// khẩu)
			// Ném một RuntimeException với thông báo lỗi rõ ràng cho người dùng
			throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không hợp lệ");
		}
	}
}