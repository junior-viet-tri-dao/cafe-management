package com.viettridao.cafe.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.Service.BanService;
import com.viettridao.cafe.Service.ChuyenBanService;
import com.viettridao.cafe.model.Ban;
import com.viettridao.cafe.model.DatBan;
import com.viettridao.cafe.repository.DatBanRepository;

@Controller
@RequestMapping("/chuyenban")
public class ChuyenBanController {

	@Autowired
	private ChuyenBanService chuyenBanService;

	@Autowired
	private BanService banService;

	@Autowired
	private DatBanRepository datBanRepo;

	@GetMapping("/{banId}")
	public String hienForm(@PathVariable Long banId, Model model) {
		model.addAttribute("banCuId", banId);
		model.addAttribute("dsBan", banService.layTatCaBanChuaXoa());
		return "ban/chuyenban";
	}

	@PostMapping
	public String xuLyChuyen(@RequestParam Long banCuId, @RequestParam Long banMoiId, RedirectAttributes redirect) {

		if (banCuId.equals(banMoiId)) {
			redirect.addFlashAttribute("error", "Không thể chuyển sang cùng một bàn.");
			return "redirect:/chuyenban/" + banCuId;
		}

		Ban banCu = banService.timTheoId(banCuId);
		Ban banMoi = banService.timTheoId(banMoiId);

		if (banCu == null || banMoi == null) {
			redirect.addFlashAttribute("error", "Không tìm thấy bàn để thực hiện chuyển.");
			return "redirect:/ban";
		}

		// ❌ ĐÃ ĐẶT TRƯỚC → ĐANG PHỤC VỤ: KHÔNG CHO
		if ("Đã Đặt Trước".equalsIgnoreCase(banCu.getTinhTrang())
				&& "Đang Phục Vụ".equalsIgnoreCase(banMoi.getTinhTrang())) {
			redirect.addFlashAttribute("error", "Không thể chuyển từ bàn ĐÃ ĐẶT TRƯỚC sang bàn ĐANG PHỤC VỤ.");
			return "redirect:/chuyenban/" + banCuId;
		}

		// ❌ ĐANG PHỤC VỤ → ĐANG PHỤC VỤ: KHÔNG CHO
		if ("Đang Phục Vụ".equalsIgnoreCase(banCu.getTinhTrang())
				&& "Đang Phục Vụ".equalsIgnoreCase(banMoi.getTinhTrang())) {
			redirect.addFlashAttribute("error", "Không thể chuyển giữa hai bàn đang phục vụ.");
			return "redirect:/chuyenban/" + banCuId;
		}
		// ❌ ĐÃ ĐẶT TRƯỚC → ĐÃ ĐẶT TRƯỚC: KHÔNG CHO
		if ("Đã Đặt Trước".equalsIgnoreCase(banCu.getTinhTrang())
				&& "Đã Đặt Trước".equalsIgnoreCase(banMoi.getTinhTrang())) {
			redirect.addFlashAttribute("error", "Không thể chuyển giữa hai bàn đã đặt trước.");
			return "redirect:/chuyenban/" + banCuId;
		}

		// ❌ ĐANG PHỤC VỤ → ĐÃ ĐẶT TRƯỚC: KHÔNG CHO
		if ("Đang Phục Vụ".equalsIgnoreCase(banCu.getTinhTrang())
				&& "Đã Đặt Trước".equalsIgnoreCase(banMoi.getTinhTrang())) {
			redirect.addFlashAttribute("error", "Không thể chuyển từ bàn ĐANG PHỤC VỤ sang bàn ĐÃ ĐẶT TRƯỚC.");
			return "redirect:/chuyenban/" + banCuId;
		}

		// ✅ Trường hợp đặc biệt: TRỐNG → ĐÃ ĐẶT TRƯỚC (chuyển đặt bàn ngược)
		if ("Trống".equalsIgnoreCase(banCu.getTinhTrang()) && "Đã Đặt Trước".equalsIgnoreCase(banMoi.getTinhTrang())) {

			Optional<DatBan> datBan = datBanRepo.findByBan_IdAndDaXoaFalse(banMoiId);
			if (datBan.isPresent()) {
				DatBan dat = datBan.get();
				dat.setBan(banCu);
				datBanRepo.save(dat);

				banCu.setTinhTrang("Đã Đặt Trước");
				banMoi.setTinhTrang("Trống");

				banService.capNhat(banCu);
				banService.capNhat(banMoi);

				redirect.addFlashAttribute("success",
						"Chuyển đặt bàn từ bàn " + banMoi.getTenBan() + " sang " + banCu.getTenBan() + " thành công.");
				return "redirect:/ban";
			}
		}

		// ✅ Chuyển bàn bình thường
		chuyenBanService.chuyenBan(banCuId, banMoiId);
		redirect.addFlashAttribute("success", "Chuyển bàn thành công!");
		return "redirect:/ban";
	}
}
