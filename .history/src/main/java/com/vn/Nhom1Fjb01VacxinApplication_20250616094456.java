package com.vn;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import com.vn.model.Users;
import com.vn.model.Role;
import com.vn.model.Gender;
import com.vn.repository.UserRepository;
import com.vn.utils.upload;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringBootApplication
public class Nhom1Fjb01VacxinApplication {

	public static void main(String[] args) {
        // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // String hashed = encoder.encode("password1");
        // System.out.println(hashed);
		SpringApplication.run(Nhom1Fjb01VacxinApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// @Bean
	// public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	// 	return args -> {
	// 		String username = "sondepzai";
	// 		if (!userRepository.existsByUsername(username)) {
	// 			Users user = new Users();
				
	// 			user.setUsername(username);
	// 			user.setPassword(passwordEncoder.encode("sondepzai"));
	// 			user.setRole(Role.Admin);
	// 			user.setFullName("Son Dep Zai");
	// 			user.setEmail("sondepzai@gmail.com");
	// 			user.setPhone("0900000000");
	// 			user.setIdentityCard("123456789");
	// 			user.setAddress("Hanoi");
	// 			user.setDateOfBirth(LocalDate.of(1995, 1, 1));
	// 			user.setGender(Gender.Male);
	// 			user.setImage("uploads/z5857141185806_a197532d78a5071a483f398ea840dcee.jpg");
	// 			userRepository.save(user);
	// 			System.out.println("Default admin user created: sondepzai");
	// 		} else {
	// 			System.out.println("Default admin user already exists: sondepzai");
	// 		}
	// 	};
	// }

}
