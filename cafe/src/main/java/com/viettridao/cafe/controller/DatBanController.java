package com.viettridao.cafe.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.Service.BanService;
import com.viettridao.cafe.Service.DatBanService;
import com.viettridao.cafe.model.Ban;
import com.viettridao.cafe.model.DatBan;
import com.viettridao.cafe.repository.DatBanRepository;

@Controller
@RequestMapping("/datban")
public class DatBanController {

	@Autowired
	private DatBanService datBanService;
	@Autowired
	private BanService banService;

	@Autowired
	private DatBanRepository datBanRepo;

	@GetMapping("/{banId}")
	public String hienThiForm(@PathVariable Long banId, Model model) {
		model.addAttribute("ban", banService.timTheoId(banId));
		model.addAttribute("datBan", new DatBan());
		return "ban/datban";
	}

	@PostMapping("/{banId}")
	public String xuLyDatBan(@PathVariable Long banId, @ModelAttribute("datBan") DatBan datBan,
			RedirectAttributes redirect) {

		Ban ban = banService.timTheoId(banId);

		// Kiểm tra trạng thái bàn
		String trangThai = ban.getTinhTrang();
		if (!trangThai.equalsIgnoreCase("Trống")) {
			redirect.addFlashAttribute("error", "Chỉ có bàn TRỐNG mới được đặt trước.");
			return "redirect:/datban/" + banId;
		}

		datBanService.datTruocBan(banId, datBan);
		redirect.addFlashAttribute("success", "Đặt bàn thành công!");
		return "redirect:/ban";
	}

	@PostMapping("/xoa/{banId}")
	public String xoaTheoBan(@PathVariable Long banId, RedirectAttributes redirect) {
		Optional<DatBan> dat = datBanRepo.findByBan_IdAndDaXoaFalse(banId);

		if (dat.isPresent()) {
			String kq = datBanService.xoaDatBan(dat.get().getId());

			if (kq.contains("không thể")) {
				redirect.addFlashAttribute("error", kq);
			} else {
				redirect.addFlashAttribute("success", kq);
			}
		} else {
			redirect.addFlashAttribute("error", "Không tìm thấy đặt bàn để hủy.");
		}

		return "redirect:/ban";
	}

}
