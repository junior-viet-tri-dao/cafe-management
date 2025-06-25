package com.viettridao.cafe.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.repository.AccountRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Lớp triển khai dịch vụ chi tiết người dùng cho Spring Security. Cung cấp
 * thông tin người dùng (UserDetails) dựa trên tên đăng nhập để hỗ trợ quá trình
 * xác thực.
 */
@Service // Đánh dấu lớp này là một Spring Service, thành phần trong tầng dịch vụ
@RequiredArgsConstructor // Tự động tạo constructor với các trường final (như accountRepository)
@Getter // Tự động tạo các phương thức getter cho các trường
public class UserServiceDetail implements UserDetailsService {
	// Repository để truy vấn thông tin tài khoản từ cơ sở dữ liệu
	private final AccountRepository accountRepository;

	/**
	 * Tải thông tin chi tiết của người dùng (UserDetails) dựa trên tên đăng nhập.
	 * Đây là phương thức bắt buộc phải triển khai của giao diện UserDetailsService,
	 * được Spring Security gọi khi cần xác thực người dùng.
	 *
	 * @param username Tên đăng nhập của người dùng cần tải thông tin.
	 * @return Đối tượng UserDetails chứa thông tin chi tiết của người dùng (thường
	 *         là AccountEntity của bạn).
	 * @throws UsernameNotFoundException nếu không tìm thấy bất kỳ tài khoản nào với
	 *                                   tên đăng nhập đã cho trong hệ thống.
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Tìm tài khoản trong cơ sở dữ liệu bằng username được cung cấp.
		// Nếu tìm thấy, trả về đối tượng AccountEntity (đã triển khai UserDetails).
		// Nếu không tìm thấy, ném ngoại lệ UsernameNotFoundException để thông báo cho
		// Spring Security.
		return accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản có username = " + username));
	}
}