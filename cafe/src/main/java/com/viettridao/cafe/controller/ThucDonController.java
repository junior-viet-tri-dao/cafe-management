package com.viettridao.cafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.Service.BanService;
import com.viettridao.cafe.Service.ThucDonService;
import com.viettridao.cafe.model.ThucDon;

@Controller
@RequestMapping("/thucdon")
public class ThucDonController {

	@Autowired
	private BanService banService;
	@Autowired
	private ThucDonService thucDonService;

	@GetMapping("/them/{banId}")
	public String hienThiFormThemMon(@PathVariable Long banId, Model model) {
		model.addAttribute("ban", banService.timTheoId(banId));
		model.addAttribute("monMoi", new ThucDon());
		return "ban/themmon";
	}

	@PostMapping("/them/{banId}")
	public String luuMon(@PathVariable Long banId, @ModelAttribute("monMoi") ThucDon monMoi) {
		thucDonService.themMon(banId, monMoi);
		return "redirect:/ban";
	}

	@PostMapping("/xoa/{id}")
	public String xoaMon(@PathVariable Long id) {
		thucDonService.xoaMon(id);
		return "redirect:/ban";
	}
}
