package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý yêu cầu liên quan đến trang chủ của ứng dụng. Cung cấp
 * endpoint để hiển thị trang chủ.
 */
@Controller
@RequiredArgsConstructor
public class HomeController {

	/**
	 * Hiển thị trang chủ của ứng dụng.
	 * 
	 * @return Tên view "layout" để hiển thị trang chủ
	 */
	@GetMapping("/home")
	public String home() {
		return "layout";
	}
}