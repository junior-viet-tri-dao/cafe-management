package com.vn.controller;

import com.vn.model.Ban;
import com.vn.model.ChiTietDatBan;
import com.vn.model.Users;
import com.vn.repository.BanRepository;
import com.vn.model.TinhTrangBan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.vn.repository.HoaDonRepository;
import com.vn.repository.UserRepository;
import com.vn.model.HoaDon;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vn.auth.CustomUserDetail;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Date;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.vn.repository.ChiTietDatBanRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.Map;

@Controller
public class BanController {
    @Autowired
    private BanRepository banRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietDatBanRepository chiTietDatBanRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/sales/table-create")
    public String showCreateTableForm(Model model) {
        model.addAttribute("table", new Ban());
        model.addAttribute("tinhTrangList", TinhTrangBan.values());
        return "admin/sales/table-create";
    }

    @PostMapping("/admin/sales/table-create")
    public String createTable(@Valid @ModelAttribute("table") Ban table, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("table", table);
            model.addAttribute("tinhTrangList", TinhTrangBan.values());
            return "admin/sales/table-create";
        }
        banRepository.save(table);
        return "redirect:/admin/sales/table-list";
    }

    @GetMapping("/admin/sales/table-list")
    public String listTable(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "") String keyword,
                            Model model) {
        Page<Ban> posts;
        if (keyword != null && !keyword.isEmpty()) {
            posts = banRepository.findAll(PageRequest.of(page, size)); // TODO: thay bằng searchBan nếu có
        } else {
            posts = banRepository.findAll(PageRequest.of(page, size));
        }
        int totalPages = posts.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 1);
            int end = Math.min(totalPages, page + 3);
            if (end - start< 4) {
                if (start == 1) {
                    end = Math.min(totalPages, start + 4);
                } else if (end == totalPages) {
                    start = Math.max(1, end - 4);
                }
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        return "admin/sales/table-list";
    }

    @GetMapping("/datban/{maBan}")
    @ResponseBody
    public Object datBan(@PathVariable Integer maBan) {
        Ban ban = banRepository.findById(maBan).orElse(null);
        if (ban == null) {
            return java.util.Map.of("success", false, "message", "Không tìm thấy bàn.");
        }
        if (ban.getTinhTrang() != null && ban.getTinhTrang().name().equals("Ranh")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            Integer maNhanVien = userDetail.getUsersDB().getMaNhanVien();
            // Tạo hóa đơn mới
            HoaDon hoaDon = new HoaDon();
            hoaDon.setNgayGioTao(new Date());
            hoaDon.setTrangThai(false);
            hoaDon = hoaDonRepository.save(hoaDon);
            return java.util.Map.of(
                "success", true,
                "maBan", maBan,
                "maHoaDon", hoaDon.getMaHoaDon(),
                "maNhanVien", maNhanVien
            );
        } else {
            return java.util.Map.of("success", false, "message", "Bàn đã có người đặt");
        }
    }

    @PostMapping("/datban/submit")
    public String submitDatBan(
        @RequestParam("tenKhachHang") String tenKhachHang,
        @RequestParam("sdtKhachHang") String sdtKhachHang,
        @RequestParam("ngayGioDat") String ngayGioDat,
        @RequestParam("maBan") Integer maBan,
        @RequestParam("maHoaDon") Integer maHoaDon,
        @RequestParam("maNhanVien") Integer maNhanVien,
        Model model) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);
        Ban ban = banRepository.findById(maBan).orElse(null);
        Users nhanVien = userRepository.findById(maNhanVien).orElse(null);
        if (hoaDon == null || ban == null) {
            model.addAttribute("message", "Không tìm thấy hóa đơn hoặc bàn để cập nhật.");
            return "modal/datban";
        }
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            java.util.Date ngayGio = sdf.parse(ngayGioDat);
            // luu ChiTietDatBan
            ChiTietDatBan chiTiet = new ChiTietDatBan();
            chiTiet.setBan(ban);
            chiTiet.setHoaDon(hoaDon);
            if (nhanVien != null) chiTiet.setNhanVien(nhanVien);
            chiTiet.setTenKhachHang(tenKhachHang);
            chiTiet.setSdtKhachHang(sdtKhachHang);
            chiTiet.setNgayGioDat(ngayGio);
            chiTietDatBanRepository.save(chiTiet);
            // update time hoadon
            hoaDon.setNgayGioTao(ngayGio);
            hoaDonRepository.save(hoaDon);
            ban.setTinhTrang(TinhTrangBan.DaDat);
            banRepository.save(ban);
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi định dạng ngày giờ đặt.");
            return "modal/datban";
        }
        return "redirect:/admin/sales/table-list";
    }
    

    @GetMapping("/xemban/{maBan}")
    @ResponseBody
    public Object xemBan(@PathVariable Integer maBan) {
    Ban ban = banRepository.findById(maBan).orElse(null);
    if (ban == null) {
        return java.util.Map.of("success", false, "message", "Không tìm thấy bàn.");
    }
    if (ban.getTinhTrang() != null) {
        if (ban.getTinhTrang().name().equals("Ranh")) {
            return java.util.Map.of("success", false, "message", "BÀN NÀY ĐANG TRỐNG MAU CHỐT KHÁCH ĐI =))");
        }
        if (ban.getTinhTrang().name().equals("DaDat")) {
            // Lấy thông tin đặt bàn như hiện tại
            List<ChiTietDatBan> list = chiTietDatBanRepository.findAll();
            ChiTietDatBan chiTiet = list.stream()
                .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
                .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
                .findFirst().orElse(null);
            if (chiTiet == null) {
                return java.util.Map.of("success", false, "message", "Không tìm thấy thông tin đặt bàn cho bàn này.");
            }
            return java.util.Map.of(
                "success", true,
                "tenKhachHang", chiTiet.getTenKhachHang(),
                "sdtKhachHang", chiTiet.getSdtKhachHang(),
                "ngayGioDat", chiTiet.getNgayGioDat() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(chiTiet.getNgayGioDat()) : "",
                "maHoaDon", chiTiet.getHoaDon() != null ? chiTiet.getHoaDon().getMaHoaDon() : null,
                "maNhanVien", chiTiet.getNhanVien() != null ? chiTiet.getNhanVien().getMaNhanVien() : null
            );
        }
        if (ban.getTinhTrang().name().equals("DangDung")) {
            // logic 
            return java.util.Map.of("success", false, "message", "Bàn đang được sử dụng, xử lý logic khác .");
        }
    }
    return java.util.Map.of("success", false, "message", "Không xác định được trạng thái bàn.");
}

    @DeleteMapping("/huyban/{maBan}")
    @ResponseBody
    public Object huyBan(@PathVariable Integer maBan) {
        Ban ban = banRepository.findById(maBan).orElse(null);
        if (ban == null) {
            return java.util.Map.of("success", false, "message", "Không tìm thấy bàn.");
        }
        if (ban.getTinhTrang() == null || ban.getTinhTrang().name().equals("Ranh")) {
            return java.util.Map.of("success", false, "message", "Khách có đâu mà hủy");
        }
        // Xóa ChiTietDatBan mới nhất của bàn này
        List<ChiTietDatBan> list = chiTietDatBanRepository.findAll();
        ChiTietDatBan chiTiet = list.stream()
            .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
            .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
            .findFirst().orElse(null);
        if (chiTiet != null) {
            chiTietDatBanRepository.delete(chiTiet);
        }
        // Set lại trạng thái bàn thành Ranh
        ban.setTinhTrang(TinhTrangBan.Ranh);
        banRepository.save(ban);
        return java.util.Map.of("success", true, "message", "Đã hủy đặt bàn thành công.");
    }


    // chuyển bàn
    @GetMapping("/danhsachbanranh")
    @ResponseBody
    public Object danhSachBanRanh(@RequestParam Integer maBanCu) {
        Ban banCu = banRepository.findById(maBanCu).orElse(null);
        if (banCu == null) {
            return java.util.Map.of("success", false, "message", "Không tìm thấy bàn cần chuyển.");
        }

        if(banCu.getTinhTrang().name().equals("DangDung")) {
            return java.util.Map.of("success", false, "message", "BÀN ĐANG ĐƯỢC SỬ DỤNG, KHÔNG THỂ CHUYỂN");
        }

        if (banCu.getTinhTrang() == null || !banCu.getTinhTrang().name().equals("DaDat")) {
            return java.util.Map.of("success", false, "message", "BÀN KHÔNG CÓ AI CHUYỂN GÌ , MAU TÌM KHÁCH");
        }
  
        List<Map<String, Object>> banRanh = banRepository.findAll().stream()
            .filter(b -> b.getTinhTrang() != null && b.getTinhTrang().name().equals("Ranh"))
            .map(b -> java.util.Map.<String, Object>of("maBan", b.getMaBan(), "tenBan", b.getTenBan()))
            .collect(java.util.stream.Collectors.toList());
        return java.util.Map.of("success", true, "data", banRanh);
    }


    @PostMapping("/chuyenban")
    @ResponseBody
    public Object chuyenBan(@RequestParam Integer maBanCu, @RequestParam Integer maBanMoi) {
        if (maBanCu.equals(maBanMoi)) {
            return java.util.Map.of("success", false, "message", "Không thể chuyển sang chính nó!");
        }
        Ban banCu = banRepository.findById(maBanCu).orElse(null);
        Ban banMoi = banRepository.findById(maBanMoi).orElse(null);
        if (banCu == null || banMoi == null) {
            return java.util.Map.of("success", false, "message", "Không tìm thấy bàn.");
        }
        if (banMoi.getTinhTrang() == null || !banMoi.getTinhTrang().name().equals("Ranh")) {
            return java.util.Map.of("success", false, "message", "Bàn mới không rảnh!");
        }
        // Lấy ChiTietDatBan mới nhất của bàn cũ
        List<ChiTietDatBan> list = chiTietDatBanRepository.findAll();
        ChiTietDatBan chiTietCu = list.stream()
            .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBanCu))
            .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
            .findFirst().orElse(null);
        if (chiTietCu == null) {
            return java.util.Map.of("success", false, "message", "Không tìm thấy chi tiết đặt bàn để chuyển.");
        }
        // Clone ChiTietDatBan with maBanMoi
        ChiTietDatBan chiTietMoi = new ChiTietDatBan();
        chiTietMoi.setBan(banMoi);
        chiTietMoi.setHoaDon(chiTietCu.getHoaDon());
        chiTietMoi.setNhanVien(chiTietCu.getNhanVien());
        chiTietMoi.setTenKhachHang(chiTietCu.getTenKhachHang());
        chiTietMoi.setSdtKhachHang(chiTietCu.getSdtKhachHang());
        chiTietMoi.setNgayGioDat(chiTietCu.getNgayGioDat());
        chiTietDatBanRepository.save(chiTietMoi);
        // delete table cũ
        chiTietDatBanRepository.delete(chiTietCu);
        // set trangthai
        banCu.setTinhTrang(TinhTrangBan.Ranh);
        banRepository.save(banCu);
        banMoi.setTinhTrang(TinhTrangBan.DaDat);
        banRepository.save(banMoi);
        return java.util.Map.of("success", true, "message", "Chuyển bàn thành công!");
    }

}

   