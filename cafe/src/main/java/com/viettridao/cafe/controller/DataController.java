package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataController {

	@GetMapping("/restore")
	public String restore(Model model) {
		return "/datas/restore";
	}

	@GetMapping("/backup")
	public String backup(Model model) {
		return "/datas/backup";
	}

	@PostMapping("/backup")
	public String handleBackup(@RequestParam("path") String path, RedirectAttributes redirectAttributes) {
		try {
			System.out.println("Backup to path: " + path);
			redirectAttributes.addFlashAttribute("success", "Đã sao lưu dữ liệu!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra khi sao lưu!");
		}
		return "redirect:/data/backup";
	}

	@PostMapping("/restore")
	public String handleRestore(@RequestParam("backupFile") MultipartFile file, RedirectAttributes redirectAttributes) {
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Vui lòng chọn file phục hồi!");
			return "redirect:/data/restore";
		}

		try {
			System.out.println("Restore from file: " + file.getOriginalFilename());
			redirectAttributes.addFlashAttribute("success", "Phục hồi dữ liệu thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra khi phục hồi!");
		}

		return "redirect:/data/restore";
	}
}
