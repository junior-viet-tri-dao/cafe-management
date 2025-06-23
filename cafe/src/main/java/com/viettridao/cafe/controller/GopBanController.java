package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.Service.BanService;
import com.viettridao.cafe.Service.GopBanService;
import com.viettridao.cafe.model.Ban;
import com.viettridao.cafe.repository.DatBanRepository;

@Controller
@RequestMapping("/gopban")
public class GopBanController {

    @Autowired
    private GopBanService gopBanService;

    @Autowired
    private BanService banService;

    @Autowired
    private DatBanRepository datBanRepo;

    @GetMapping
    public String hienForm(Model model) {
        model.addAttribute("dsBan", banService.layTatCaBanChuaXoa());
        return "ban/gopban";
    }

    @PostMapping
    public String xuLyGop(@RequestParam Long banChinhId,
                           @RequestParam(required = false) List<Long> banPhuIds,
                           RedirectAttributes redirect) {

        if (banPhuIds == null || banPhuIds.isEmpty()) {
            redirect.addFlashAttribute("error", "Bạn phải chọn ít nhất 1 bàn để gộp.");
            return "redirect:/gopban";
        }

        if (banPhuIds.contains(banChinhId)) {
            redirect.addFlashAttribute("error", "Không thể gộp bàn chính vào chính nó.");
            return "redirect:/gopban";
        }

        Ban banChinh = banService.timTheoId(banChinhId);
        if (banChinh == null) {
            redirect.addFlashAttribute("error", "Không tìm thấy bàn chính.");
            return "redirect:/gopban";
        }

        boolean datBanChinh = datBanRepo.findByBan_IdAndDaXoaFalse(banChinhId).isPresent();

        for (Long id : banPhuIds) {
            Ban banPhu = banService.timTheoId(id);
            if (banPhu == null) continue;

            String ttChinh = banChinh.getTinhTrang();
            String ttPhu = banPhu.getTinhTrang();

            if ("Đã Đặt Trước".equalsIgnoreCase(ttChinh) && "Đang Phục Vụ".equalsIgnoreCase(ttPhu)) {
                redirect.addFlashAttribute("error", "Không thể gộp bàn ĐANG PHỤC VỤ vào bàn ĐÃ ĐẶT TRƯỚC.");
                return "redirect:/gopban";
            }

            if ("Đang Phục Vụ".equalsIgnoreCase(ttChinh) && "Đã Đặt Trước".equalsIgnoreCase(ttPhu)) {
                redirect.addFlashAttribute("error", "Không thể gộp bàn ĐÃ ĐẶT TRƯỚC vào bàn ĐANG PHỤC VỤ.");
                return "redirect:/gopban";
            }

            if ("Đã Đặt Trước".equalsIgnoreCase(ttChinh) && "Đã Đặt Trước".equalsIgnoreCase(ttPhu)) {
                redirect.addFlashAttribute("error", "Không thể gộp hai bàn đều đã được ĐẶT TRƯỚC.");
                return "redirect:/gopban";
            }

            if ("Trống".equalsIgnoreCase(ttChinh) && "Đã Đặt Trước".equalsIgnoreCase(ttPhu)) {
                redirect.addFlashAttribute("error", "Không thể gộp bàn ĐÃ ĐẶT TRƯỚC vào bàn TRỐNG.");
                return "redirect:/gopban";
            }

            if ("Đã Đặt Trước".equalsIgnoreCase(ttChinh) && "Trống".equalsIgnoreCase(ttPhu)) {
                redirect.addFlashAttribute("error", "Không thể gộp bàn TRỐNG vào bàn ĐÃ ĐẶT TRƯỚC.");
                return "redirect:/gopban";
            }

            boolean datBanPhu = datBanRepo.findByBan_IdAndDaXoaFalse(id).isPresent();
            if (datBanChinh && datBanPhu) {
                redirect.addFlashAttribute("error", "Không thể gộp: cả hai bàn đều đã được đặt trước.");
                return "redirect:/gopban";
            }
        }

        gopBanService.gopBan(banChinhId, banPhuIds);
        redirect.addFlashAttribute("success", "Gộp bàn thành công!");
        return "redirect:/ban";
    }
}
