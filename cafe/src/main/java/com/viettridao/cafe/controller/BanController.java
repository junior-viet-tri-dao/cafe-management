package com.viettridao.cafe.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.Service.BanService;
import com.viettridao.cafe.model.Ban;
import com.viettridao.cafe.model.DatBan;
import com.viettridao.cafe.model.NhanVien;
import com.viettridao.cafe.model.ThucDon;
import com.viettridao.cafe.model.VaiTro;
import com.viettridao.cafe.repository.DatBanRepository;
import com.viettridao.cafe.repository.ThucDonRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ban")
public class BanController {

    @Autowired
    private BanService banService;

    @Autowired
    private ThucDonRepository thucDonRepo;

    @Autowired
    private DatBanRepository datBanRepo;

    // ✅ Hiển thị danh sách bàn (phân quyền ADMIN vs NHANVIEN)
    @GetMapping
    public String hienThiBan(HttpSession session, Model model) {
        NhanVien nv = (NhanVien) session.getAttribute("nv");

        if (nv == null) return "redirect:/login";

        model.addAttribute("nv", nv); // truyền cho Thymeleaf phân quyền
        model.addAttribute("dsBan", banService.layTatCaBanChuaXoa());

        if (nv.getVaiTro() == VaiTro.ADMIN) {
            return "ban/list"; // ADMIN xem full chức năng
        } else {
            return "ban/list"; // NHÂN VIÊN cũng dùng chung, view kiểm soát chức năng
        }
    }

    // ✅ Xem chi tiết bàn (thông tin bàn, món, đặt bàn)
    @GetMapping("/{id}/xem")
    public String xemThongTinBan(@PathVariable Long id, Model model) {
        Ban ban = banService.timTheoId(id);
        List<ThucDon> dsMon = thucDonRepo.findByBan_IdAndDaXoaFalse(id);
        Optional<DatBan> datBan = datBanRepo.findByBan_IdAndDaXoaFalse(id);

        model.addAttribute("ban", ban);
        model.addAttribute("dsMon", dsMon);
        model.addAttribute("datBan", datBan.orElse(null));

        return "ban/xemban";
    }
}
