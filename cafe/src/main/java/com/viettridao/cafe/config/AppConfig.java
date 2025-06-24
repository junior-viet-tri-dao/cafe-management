package com.viettridao.cafe.config;

import com.viettridao.cafe.service.UserServiceDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Lớp cấu hình bảo mật cho ứng dụng sử dụng Spring Security. Thiết lập các quy
 * tắc xác thực, phân quyền, và mã hóa mật khẩu.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfig {
	// Danh sách các endpoint không yêu cầu xác thực
	private static final String[] AUTH_WHITELIST = { "/login", "/js/**" };

	// Dịch vụ chi tiết người dùng để tải thông tin tài khoản
	private final UserServiceDetail userServiceDetail;

	/**
	 * Cấu hình chuỗi bộ lọc bảo mật cho các yêu cầu HTTP.
	 * 
	 * @param http Đối tượng HttpSecurity để cấu hình bảo mật
	 * @return SecurityFilterChain đã được cấu hình
	 * @throws Exception Nếu có lỗi trong quá trình cấu hình
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				// Kích hoạt bảo vệ CSRF với cấu hình mặc định
				.csrf(Customizer.withDefaults())
				// Kích hoạt hỗ trợ CORS với cấu hình mặc định
				.cors(withDefaults())
				// Cấu hình phân quyền cho các yêu cầu HTTP
				.authorizeHttpRequests(request -> request
						// Cho phép truy cập không cần xác thực cho các endpoint trong AUTH_WHITELIST
						.requestMatchers(AUTH_WHITELIST).permitAll()
						// Yêu cầu xác thực cho tất cả các yêu cầu khác
						.anyRequest().authenticated())
				// Tắt form login mặc định của Spring Security
				.formLogin(AbstractHttpConfigurer::disable)
				// Tắt xác thực HTTP Basic
				.httpBasic(AbstractHttpConfigurer::disable)
				// Sử dụng AuthenticationProvider tùy chỉnh
				.authenticationProvider(authenticationProvider())
				// Cấu hình đăng xuất
				.logout(logout -> logout
						// URL để xử lý đăng xuất
						.logoutUrl("/logout")
						// Chuyển hướng sau khi đăng xuất thành công
						.logoutSuccessUrl("/login?logout")
						// Hủy session sau khi đăng xuất
						.invalidateHttpSession(true)
						// Xóa cookie JSESSIONID
						.deleteCookies("JSESSIONID")
						// Cho phép tất cả truy cập endpoint đăng xuất
						.permitAll());

		// Trả về chuỗi bộ lọc bảo mật đã cấu hình
		return http.build();
	}

	/**
	 * Cấu hình AuthenticationProvider sử dụng DaoAuthenticationProvider.
	 * 
	 * @return AuthenticationProvider đã được cấu hình
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		// Tạo nhà cung cấp xác thực dựa trên DAO
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		// Thiết lập dịch vụ chi tiết người dùng
		provider.setUserDetailsService(userServiceDetail);
		// Thiết lập bộ mã hóa mật khẩu
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	/**
	 * Cấu hình AuthenticationManager từ AuthenticationConfiguration.
	 * 
	 * @param config Cấu hình xác thực của Spring Security
	 * @return AuthenticationManager để xử lý xác thực
	 * @throws Exception Nếu có lỗi trong quá trình lấy AuthenticationManager
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		// Trả về AuthenticationManager từ cấu hình
		return config.getAuthenticationManager();
	}

	/**
	 * Cấu hình PasswordEncoder sử dụng BCrypt với độ mạnh 6.
	 * 
	 * @return PasswordEncoder để mã hóa mật khẩu
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		// Trả về BCryptPasswordEncoder với độ mạnh 6
		return new BCryptPasswordEncoder(6);
	}
}