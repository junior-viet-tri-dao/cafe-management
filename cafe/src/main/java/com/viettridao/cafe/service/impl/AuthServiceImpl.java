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
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
	// Quản lý xác thực của Spring Security
	private final AuthenticationManager authenticationManager;
	// Đối tượng request để làm việc với HTTP session
	private final HttpServletRequest request;

	/**
	 * Phương thức xử lý đăng nhập người dùng.
	 * 
	 * @param username Tên đăng nhập của người dùng
	 * @param password Mật khẩu của người dùng
	 * @return true nếu đăng nhập thành công
	 * @throws RuntimeException nếu thông tin đăng nhập không hợp lệ hoặc thiếu
	 */
	@Override
	public boolean login(String username, String password) {
		// Kiểm tra xem username và password có được cung cấp hay không
		if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
			throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
		}

		try {
			// Tạo đối tượng xác thực với username và password
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			// Lưu thông tin xác thực vào SecurityContext
			SecurityContextHolder.getContext().setAuthentication(authentication);
			// Tạo hoặc lấy session hiện tại
			HttpSession session = request.getSession(true);
			// Lưu thông tin xác thực vào session để duy trì trạng thái đăng nhập
			session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
			// Trả về true nếu đăng nhập thành công
			return true;
		} catch (AuthenticationException e) {
			// Ném ngoại lệ nếu xác thực thất bại (sai username hoặc password)
			throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không hợp lệ");
		}
	}
}
