package com.vn.controller;

import com.vn.model.Ban;
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

@Controller
public class BanController {
    @Autowired
    private BanRepository banRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

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
            ban.setTinhTrang(TinhTrangBan.DaDat);
            banRepository.save(ban);
            // Lấy nhân viên hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            Integer maNhanVien = userDetail.getUsersDB().getMaNhanVien();
            // Tạo hóa đơn mới
            HoaDon hoaDon = new HoaDon();
            int randomMaHoaDon = (int) (Math.random() * 900000) + 100000;
            hoaDon.setMaHoaDon(randomMaHoaDon);
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
    public String submitDatBan(@RequestParam("tenKhachHang") String tenKhachHang,
                              @RequestParam("sdtKhachHang") String sdtKhachHang,
                              @RequestParam("ngayDat") String ngayDat,
                              @RequestParam("gioDat") String gioDat,
                              @RequestParam("maBan") Integer maBan,
                              @RequestParam("maHoaDon") Integer maHoaDon,
                              @RequestParam("maNhanVien") Integer maNhanVien,
                              Model model) {
        // Xử lý lưu thông tin đặt bàn
        // 1. Lưu thông tin khách hàng vào HoaDon (nếu cần, hoặc tạo entity mới nếu có)
        // 2. Lưu ngày giờ đặt (gộp date + time)
        // 3. Gán lại các thông tin vào HoaDon
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);
        if (hoaDon == null) {
            model.addAttribute("message", "Không tìm thấy hóa đơn để cập nhật.");
            return "modal/datban";
        }
        // Nếu entity HoaDon chưa có các trường này, bạn cần mở rộng entity và DB
        // Ở đây sẽ dùng thuộc tính tạm thời (nếu có), hoặc bạn có thể tạo entity KhachHang, ChiTietDatBan,...
        // Tạm thời chỉ cập nhật ngày giờ đặt vào trường ngayGioTao nếu muốn
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.util.Date ngayGioDat = sdf.parse(ngayDat + " " + gioDat);
            hoaDon.setNgayGioTao(ngayGioDat); // hoặc tạo trường mới nếu cần
            hoaDonRepository.save(hoaDon);
        } catch (Exception e) {
            model.addAttribute("message", "Lỗi định dạng ngày giờ đặt.");
            return "modal/datban";
        }
        // Có thể lưu thêm thông tin khách hàng vào bảng khác nếu cần
        // Chuyển hướng về danh sách bàn hoặc trang xác nhận
        return "redirect:/admin/sales/table-list";
    }
}