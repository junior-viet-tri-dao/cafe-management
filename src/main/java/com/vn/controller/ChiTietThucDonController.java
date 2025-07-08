package com.vn.controller;
import com.vn.model.ChiTietThucDon;
import com.vn.model.ThucDon;
import com.vn.model.HangHoa;
import com.vn.repository.ChiTietThucDonRepository;
import com.vn.repository.ThucDonRepository;
import com.vn.repository.HangHoaRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/thucdon")
@RequiredArgsConstructor
public class ChiTietThucDonController {
    private final ThucDonRepository thucDonRepository;
    private final HangHoaRepository hangHoaRepository;
    private final ChiTietThucDonRepository chiTietThucDonRepository;

    @GetMapping("/chitietthucdon-create")
    public String showCreateForm(Model model, @RequestParam(value = "maThucDon", required = false) Integer maThucDon) {
        List<ThucDon> thucDons = thucDonRepository.findAll();
        List<HangHoa> hangHoas = hangHoaRepository.findAll();
        model.addAttribute("thucDons", thucDons);
        model.addAttribute("hangHoas", hangHoas);
        model.addAttribute("selectedThucDon", maThucDon);
        model.addAttribute("chiTietThucDonForm", new ChiTietThucDonForm());
        return "admin/thucdon/chitietthucdon-create";
    }

    @PostMapping("/chitietthucdon-create")
    public String createChiTietThucDon(@ModelAttribute ChiTietThucDonForm chiTietThucDonForm, Model model, RedirectAttributes redirectAttributes) {
        Optional<ThucDon> thucDonOpt = thucDonRepository.findById(chiTietThucDonForm.getMaThucDon());
        if (thucDonOpt.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy món ăn!");
            return "admin/thucdon/chitietthucdon-create";
        }
        ThucDon thucDon = thucDonOpt.get();
        List<ChiTietThucDon> chiTietList = new ArrayList<>();
        for (ChiTietThucDonForm.ThanhPhan tp : chiTietThucDonForm.getThanhPhanList()) {
            Optional<HangHoa> hangHoaOpt = hangHoaRepository.findById(tp.getMaHangHoa());
            if (hangHoaOpt.isEmpty()) continue;
            ChiTietThucDon cttd = new ChiTietThucDon();
            cttd.setThucDon(thucDon);
            cttd.setHangHoa(hangHoaOpt.get());
            cttd.setKhoiLuong(tp.getKhoiLuong());
            cttd.setDonViTinh(tp.getDonViTinh());
            chiTietList.add(cttd);
        }
        chiTietThucDonRepository.saveAll(chiTietList);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm chi tiết thực đơn thành công!");
        return "redirect:/admin/thucdon/chitietthucdon-create";
    }

    // Form backing object
    @Data
    public static class ChiTietThucDonForm {
        private Integer maThucDon;
        private List<ThanhPhan> thanhPhanList = new ArrayList<>();
        @Data
        public static class ThanhPhan {
            private Integer maHangHoa;
            private Double khoiLuong;
            private String donViTinh;
        }
    }
} 