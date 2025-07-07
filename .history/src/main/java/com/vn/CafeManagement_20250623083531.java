package com.vn;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.vn.model.Users;
import com.vn.model.Role;
import com.vn.model.GioiTinh;
import com.vn.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class CafeManagement {

	public static void main(String[] args) {
        // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // String hashed = encoder.encode("password1");
        // System.out.println(hashed);
		SpringApplication.run(CafeManagement.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

@Bean
	public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			String username = "sondepzai2";
			if (!userRepository.existsByUsername(username)) {
				Users user = new Users();
				
				user.setUsername(username);
				user.setPassword(passwordEncoder.encode("sondepzai2"));
				user.setRole(Role.Customer);
				user.setHoTen("Son Dep Zai 2");
				user.setEmail("sondepzai2@gmail.com");
				user.setSoDienThoai("0900000002");
				user.setCMND("123456782");
				user.setDiaChi("Hanoi");
				user.setGioiTinh(GioiTinh.Male);
				user.setImage("uploads/sondepzai2.jpg");
				userRepository.save(user);
				System.out.println("Default customer user created: sondepzai");
			} else {
				System.out.println("Default admin user already exists: sondepzai");
			}
		};
	}

	@Bean
public CommandLineRunner createDefaultAdminAccount(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
        String username = "admin";
        if (!userRepository.existsByUsername(username)) {
            Users user = new Users();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setRole(Role.Admin); // Đặt role là Admin
            user.setHoTen("Admin User");
            user.setEmail("admin@gmail.com");
            user.setSoDienThoai("0900000000");
            user.setCMND("123456789");
            user.setDiaChi("Hanoi");
            user.setGioiTinh(GioiTinh.Male);
            user.setImage("uploads/sondepzai.jpg");
            userRepository.save(user);
            System.out.println("Default admin account created: admin");
        } else {
            System.out.println("Default admin account already exists: admin");
        }
    };
}


}
