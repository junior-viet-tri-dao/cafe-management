package com.viettridao.cafe.service;

import com.viettridao.cafe.repository.AccountRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Lớp triển khai dịch vụ chi tiết người dùng cho Spring Security. Cung cấp
 * thông tin người dùng dựa trên tên đăng nhập để hỗ trợ xác thực.
 */
@Service
@RequiredArgsConstructor
@Getter
public class UserServiceDetail implements UserDetailsService {
	// Repository để truy vấn thông tin tài khoản từ cơ sở dữ liệu
	private final AccountRepository accountRepository;

	/**
	 * Tải thông tin chi tiết của người dùng dựa trên tên đăng nhập.
	 * 
	 * @param username Tên đăng nhập của người dùng
	 * @return UserDetails chứa thông tin chi tiết của người dùng
	 * @throws UsernameNotFoundException nếu không tìm thấy tài khoản với tên đăng
	 *                                   nhập đã cho
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Tìm tài khoản theo username, ném ngoại lệ nếu không tìm thấy
		return accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản có username = " + username));
	}
}