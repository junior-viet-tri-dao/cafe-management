package com.vn.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.model.*;
import com.vn.repository.BanRepository;
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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vn.auth.CustomUserDetail;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.HashMap;

import jakarta.validation.Valid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.vn.repository.ChiTietDatBanRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.Map;
import com.vn.repository.ThucDonRepository;
import com.vn.repository.ChiTietHoaDonRepository;

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

    @Autowired
    private ThucDonRepository thucDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

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
            HoaDon hoaDon = new HoaDon();
            hoaDon.setNgayGioTao(new Date());
            hoaDon.setTrangThai(false);
            hoaDon = hoaDonRepository.save(hoaDon);
            return Map.of(
                "success", true,
                "maBan", maBan,
                "maHoaDon", hoaDon.getMaHoaDon(),
                "maNhanVien", maNhanVien
            );
        } else {
            return Map.of("success", false, "message", "Bàn đã có người đặt");
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
        Model model,
        RedirectAttributes redirectAttributes) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);
        Ban ban = banRepository.findById(maBan).orElse(null);
        Users nhanVien = userRepository.findById(maNhanVien).orElse(null);
        if (hoaDon == null || ban == null) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy hóa đơn hoặc bàn để cập nhật.");
            return "modal/datban";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date ngayGio = sdf.parse(ngayGioDat);
            ChiTietDatBan chiTiet = new ChiTietDatBan();
            chiTiet.setBan(ban);
            chiTiet.setHoaDon(hoaDon);
            if (nhanVien != null) chiTiet.setNhanVien(nhanVien);
            chiTiet.setTenKhachHang(tenKhachHang);
            chiTiet.setSdtKhachHang(sdtKhachHang);
            chiTiet.setNgayGioDat(ngayGio);
            chiTietDatBanRepository.save(chiTiet);
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
        return Map.of("success", false, "message", "Không tìm thấy bàn.");
    }
    if (ban.getTinhTrang() != null) {
        if (ban.getTinhTrang().name().equals("Ranh")) {
            return Map.of("success", false, "message", "BÀN NÀY ĐANG TRỐNG MAU CHỐT KHÁCH ĐI =))");
        }
        if (ban.getTinhTrang().name().equals("DaDat")) {
            List<ChiTietDatBan> list = chiTietDatBanRepository.findAll(); // get chitietdatban
            ChiTietDatBan chiTiet = list.stream()
                .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
                .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
                .findFirst().orElse(null);
            if (chiTiet == null) {
                return Map.of("success", false, "message", "Không tìm thấy thông tin đặt bàn cho bàn này.");
            }
            return Map.of(
                "success", true,
                "tenKhachHang", chiTiet.getTenKhachHang(),
                "sdtKhachHang", chiTiet.getSdtKhachHang(),
                "ngayGioDat", chiTiet.getNgayGioDat() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(chiTiet.getNgayGioDat()) : "",
                "maHoaDon", chiTiet.getHoaDon() != null ? chiTiet.getHoaDon().getMaHoaDon() : null,
                "maNhanVien", chiTiet.getNhanVien() != null ? chiTiet.getNhanVien().getMaNhanVien() : null
            );
        }
        if (ban.getTinhTrang().name().equals("DangDung")) {
            List<ChiTietDatBan> list = chiTietDatBanRepository.findAll();
            ChiTietDatBan chiTiet = list.stream()
                .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
                .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
                .findFirst().orElse(null);
            if(chiTiet == null || chiTiet.getHoaDon() == null) {
                return Map.of("success", false, "message", "Không tìm thấy thông tin đặt bàn cho bàn này.");
            }

            Integer maHoaDon = chiTiet.getHoaDon().getMaHoaDon();
            // lay danh sach cac mon goi trong chi tiet hoa don
            List<ChiTietHoaDon> dsMon = chiTietHoaDonRepository.findAll().stream()
        .filter(ct -> ct.getHoaDon() != null && ct.getHoaDon().getMaHoaDon().equals(maHoaDon))
        .collect(Collectors.toList());
            List<Map<String, Object>> monList = new ArrayList<>();
            for (ChiTietHoaDon ct : dsMon) {
                Map<String, Object> mon = new HashMap<>();
                mon.put("maThucDon", ct.getThucDon().getMaThucDon());
                mon.put("tenMon", ct.getThucDon().getTenMon());
                mon.put("soLuong", ct.getSoLuong());
                mon.put("giaTaiThoiDiemBan", ct.getGiaTaiThoiDiemBan());
                mon.put("thanhTien", ct.getThanhTien());
                monList.add(mon);
            }
            return Map.of(
                "success", true,
                "tenKhachHang", chiTiet.getTenKhachHang(),
                "sdtKhachHang", chiTiet.getSdtKhachHang(),
                "ngayGioDat", chiTiet.getNgayGioDat() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(chiTiet.getNgayGioDat()) : "",
                "maHoaDon", chiTiet.getHoaDon().getMaHoaDon(),
                "maNhanVien", chiTiet.getNhanVien() != null ? chiTiet.getNhanVien().getMaNhanVien() : null,
                "monList", monList
            );
        }
    }
    return Map.of("success", false, "message", "Không xác định được trạng thái bàn.");
}

    @DeleteMapping("/huyban/{maBan}")
    @ResponseBody
    public Object huyBan(@PathVariable Integer maBan) {
        Ban ban = banRepository.findById(maBan).orElse(null);
        if (ban == null) {
            return Map.of("success", false, "message", "Không tìm thấy bàn.");
        }
        if (ban.getTinhTrang() == null || ban.getTinhTrang().name().equals("Ranh")) {
            return Map.of("success", false, "message", "Khách có đâu mà hủy");
        }

        List<ChiTietDatBan> list = chiTietDatBanRepository.findAll();
        ChiTietDatBan chiTiet = list.stream()
            .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
            .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
            .findFirst().orElse(null);

        if (chiTiet != null) {
            chiTietDatBanRepository.delete(chiTiet);
        }

        ban.setTinhTrang(TinhTrangBan.Ranh);
        banRepository.save(ban);
        return Map.of("success", true, "message", "Đã hủy đặt bàn thành công.");
    }


    // chuyển bàn
    @GetMapping("/danhsachbanranh")
    @ResponseBody
    public Object danhSachBanRanh(@RequestParam Integer maBanCu) {

    Ban banCu = banRepository.findById(maBanCu).orElse(null);
    if (banCu == null) {
        return Map.of("success", false, "message", "Không tìm thấy bàn cần chuyển.");
    }

    TinhTrangBan tinhTrang = banCu.getTinhTrang();
    if (tinhTrang == null) {
        return Map.of("success", false, "message", "Không xác định được trạng thái bàn.");
    }

    if (tinhTrang == TinhTrangBan.DangDung) {
        return Map.of("success", false, "message", "BÀN ĐANG ĐƯỢC SỬ DỤNG, KHÔNG THỂ CHUYỂN");}
        
        
    else if (tinhTrang == TinhTrangBan.DaDat) {

        // C1
        // List<Map<String, Object>> banRanh = banRepository.findAll().stream()
        //         .filter(b -> b.getTinhTrang() == TinhTrangBan.Ranh)
        //         .map(b -> Map.<String, Object>of("maBan", b.getMaBan(), "tenBan", b.getTenBan()))
        //         .collect(Collectors.toList());

        // C2 :
        // List<Ban> allBan = banRepository.findAll();
        // List<Map<String, Object>> banRanh = new ArrayList<>();
        // for (Ban b : allBan) {
        //     if(b.getTinhTrang() == TinhTrangBan.Ranh) {
        //         Map<String, Object> map = new HashMap<>();
        //         map.put("maBan", b.getMaBan());
        //         map.put("tenBan", b.getTenBan());
        //         banRanh.add(map);
        //     }
        // }

        //C3 : sql repo dùng object
        List<Object[]> rawBanRanh = banRepository.findMaBanAndTenBanByTinhTrang(TinhTrangBan.Ranh);
        List<Map<String, Object>> banRanh = new ArrayList<>();
        for(Object[] arr : rawBanRanh) {
            Map<String, Object> map = new HashMap<>();
            map.put("maBan", arr[0]);
            map.put("tenBan", arr[1]);
            banRanh.add(map);
        }
            
        return Map.of("success", true, "data", banRanh);
    } else {
        return Map.of("success", false, "message", "BÀN KHÔNG CÓ AI CHUYỂN GÌ , MAU TÌM KHÁCH");
    }
}

    @PostMapping("/chuyenban")
    @ResponseBody
    public Object chuyenBan(@RequestParam Integer maBanCu, @RequestParam Integer maBanMoi) {

        if (maBanCu.equals(maBanMoi)) {
            return Map.of("success", false, "message", "Không thể chuyển sang chính nó!");
        }
        Ban banCu = banRepository.findById(maBanCu).orElse(null);
        Ban banMoi = banRepository.findById(maBanMoi).orElse(null);
        if (banCu == null || banMoi == null) {
            return Map.of("success", false, "message", "Không tìm thấy bàn.");
        }
        if (banMoi.getTinhTrang() == null || !banMoi.getTinhTrang().name().equals("Ranh")) {
            return Map.of("success", false, "message", "Bàn mới không rảnh!");
        }
        // lấy ChiTietDatBan mới nhất của bàn cũ
        List<ChiTietDatBan> list = chiTietDatBanRepository.findAll();
        ChiTietDatBan chiTietCu = list.stream()
            .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBanCu))
            .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
            .findFirst().orElse(null);
        if (chiTietCu == null) {
            return Map.of("success", false, "message", "Không tìm thấy chi tiết đặt bàn để chuyển.");
        }
        // Clone 
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
        return Map.of("success", true, "message", "Chuyển bàn thành công!");
    }


    @GetMapping("/thucdon/{maBan}")
    @ResponseBody
    public Object layThucDonTheoBan(@PathVariable Integer maBan) {
        Ban ban = banRepository.findById(maBan).orElse(null);
        if (ban == null) {
            return java.util.Map.of("success", false, "message", "Không tìm thấy bàn.");
        }
        TinhTrangBan tinhTrang = ban.getTinhTrang();
        if (tinhTrang == null || tinhTrang == TinhTrangBan.Ranh) {
            return java.util.Map.of("success", false, "message", "Có khách đâu mà đặt món!");
        }
        // lấy maHoaDon mới nhất của bàn này
        List<ChiTietDatBan> list = chiTietDatBanRepository.findAll();
        ChiTietDatBan chiTiet = list.stream()
            .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
            .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
            .findFirst().orElse(null);
        if (chiTiet == null || chiTiet.getHoaDon() == null) {
            return Map.of("success", false, "message", "Không tìm thấy hóa đơn cho bàn này.");
        }
        Integer maHoaDon = chiTiet.getHoaDon().getMaHoaDon();
        // get list thực đơn
        List<ThucDon> thucDonList = thucDonRepository.findAll();
    
        List<Map<String, Object>> thucDonData = thucDonList.stream().map(td -> {
        Map<String, Object> map = new HashMap<>();
        map.put("maThucDon", td.getMaThucDon());
        map.put("tenMon", td.getTenMon());
        map.put("giaTienHienTai", td.getGiaTienHienTai());
        return map;
        }).collect(Collectors.toList());
        return Map.of(
            "success", true,
            "maHoaDon", maHoaDon,
            "thucDonList", thucDonData
        );
    }

    @PostMapping("/thucdon/goi")
    @ResponseBody
    public Object goiMon(@RequestParam Integer maHoaDon, @RequestParam List<Integer> maThucDon, @RequestParam List<Integer> soLuong) {
        if (maThucDon.size() != soLuong.size()) {
            return Map.of("success", false, "message", "Dữ liệu gửi lên không hợp lệ.");
        }
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);
        if (hoaDon == null) {
            return Map.of("success", false, "message", "Không tìm thấy hóa đơn.");
        }
        double tongTien = 0;
        for (int i = 0; i < maThucDon.size(); i++) {
            Integer maTD = maThucDon.get(i);
            Integer sl = soLuong.get(i);
            ThucDon td = thucDonRepository.findById(maTD).orElse(null);
            if (td == null) continue;
            double gia = td.getGiaTienHienTai();
            double thanhTien = gia * sl;
            tongTien += thanhTien;
            ChiTietHoaDon cthd = chiTietHoaDonRepository.findByHoaDonAndThucDon(hoaDon, td); // check chi tiet hoa don
            if (cthd != null) {
                int soLuongMoi = cthd.getSoLuong() + sl; // cộng dồn sl và thanh tien
                double thanhTienMoi = cthd.getGiaTaiThoiDiemBan() * soLuongMoi;
                cthd.setSoLuong(soLuongMoi);
                cthd.setThanhTien(thanhTienMoi);
                chiTietHoaDonRepository.save(cthd);
            } else { // tao moi
                cthd = new ChiTietHoaDon();
                cthd.setHoaDon(hoaDon);
                cthd.setThucDon(td);
                cthd.setSoLuong(sl);
                cthd.setGiaTaiThoiDiemBan(gia);
                cthd.setThanhTien(thanhTien);
                chiTietHoaDonRepository.save(cthd);
                ChiTietDatBan chiTietDatBan = null;
                Date maxDate = null;
                for (ChiTietDatBan c : chiTietDatBanRepository.findAll()) {
                    if (c.getHoaDon() != null && c.getHoaDon().getMaHoaDon().equals(maHoaDon)) {
                        if (maxDate == null || c.getNgayGioDat().after(maxDate)) {
                            maxDate = c.getNgayGioDat();
                            chiTietDatBan = c;
                        }
                    }
                }
                if (chiTietDatBan != null && chiTietDatBan.getBan() != null) {
                    Ban ban = chiTietDatBan.getBan();
                    ban.setTinhTrang(TinhTrangBan.DangDung);
                    banRepository.save(ban);
                }
            }
        }
        
        return Map.of("success", true, "message", "Gọi món thành công!", "tongTien", tongTien);
    }

    @GetMapping("/thanhtoan/{maBan}")
    @ResponseBody
    public Object getThanhToan(@PathVariable Integer maBan) {
        Ban ban = banRepository.findById(maBan).orElse(null);
        if (ban == null) {
            return Map.of("success", false, "message", "Không tìm thấy bàn.");
        }
        if (ban.getTinhTrang() == null || !ban.getTinhTrang().name().equals("DangDung")) {
            return Map.of("success", false, "message", "Mau chốt khách và chốt món cho khách!");
        }
        List<ChiTietDatBan> listCTDB = chiTietDatBanRepository.findAll();
        ChiTietDatBan chiTietDatBan = listCTDB.stream()
            .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
            .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
            .findFirst().orElse(null);
        if (chiTietDatBan == null) {
            return Map.of("success", false, "message", "Không tìm thấy hóa đơn cho bàn này.");
        }
        Integer maHoaDon = chiTietDatBan.getHoaDon().getMaHoaDon();
        List<ChiTietHoaDon> chiTietHoaDonList = chiTietHoaDonRepository.findAll().stream()
            .filter(ct -> ct.getHoaDon() != null && ct.getHoaDon().getMaHoaDon().equals(maHoaDon))
            .collect(java.util.stream.Collectors.toList());
        List<Map<String, Object>> monList = new ArrayList<>();
        double tongTien = 0;
        for (ChiTietHoaDon cthd : chiTietHoaDonList) {
            String tenMon = cthd.getThucDon() != null ? cthd.getThucDon().getTenMon() : "";
            Integer soLuong = cthd.getSoLuong();
            Double thanhTien = cthd.getThanhTien();
            tongTien += (thanhTien != null ? thanhTien : 0);
            monList.add(Map.of(
                "tenMon", tenMon,
                "soLuong", soLuong,
                "thanhTien", thanhTien
            ));
        }
        return Map.of(
            "success", true,
            "maBan", maBan,
            "maHoaDon", maHoaDon,
            "monList", monList,
            "tongTien", tongTien
        );
    }


    @PostMapping("/thanhtoan")
    @ResponseBody
    public Object postThanhToan(@RequestParam Integer maBan, @RequestParam Integer maHoaDon, @RequestParam Double tongTien) {
        
        Ban ban = banRepository.findById(maBan).orElse(null);
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).orElse(null);
        if (ban == null || hoaDon == null) {
            return Map.of("success", false, "message", "Không tìm thấy bàn hoặc hóa đơn.");
        }
        
        List<ChiTietDatBan> listCTDB = chiTietDatBanRepository.findAll();
        ChiTietDatBan chiTietDatBan = listCTDB.stream()
            .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
            .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
            .findFirst().orElse(null);
        if (chiTietDatBan == null) {
            return Map.of("success", false, "message", "Không tìm thấy chi tiết đặt bàn cho bàn này.");
        }   
        if(chiTietDatBan != null){
            chiTietDatBanRepository.delete(chiTietDatBan);
        }
       
        List<ChiTietHoaDon> chiTietHoaDonList = chiTietHoaDonRepository.findAll().stream()
            .filter(ct -> ct.getHoaDon() != null && ct.getHoaDon().getMaHoaDon().equals(maHoaDon))
            .collect(Collectors.toList());
        for (ChiTietHoaDon cthd : chiTietHoaDonList) {
            chiTietHoaDonRepository.delete(cthd);
        }

        ban.setTinhTrang(TinhTrangBan.Ranh);
        banRepository.save(ban);
        hoaDon.setTongTien(tongTien);
        hoaDon.setTrangThai(true);
        hoaDonRepository.save(hoaDon);
        return Map.of("success", true, "message", "Thanh toán thành công!");
    }


    
    @GetMapping("/gopban/chonbandich")
    @ResponseBody
    public Object chonBanDich(@RequestParam("banNguon") String banNguonStr) {
        String[] arr = banNguonStr.split(",");
        List<Integer> banNguon = new ArrayList<>();
        for (String s : arr) {
            try { banNguon.add(Integer.parseInt(s)); } catch (Exception ignored) {}
        }
        List<Ban> allBan = banRepository.findAll();
        List<Map<String, Object>> banDichList = new ArrayList<>();
        for (Ban b : allBan) {
            if (b.getTinhTrang() == TinhTrangBan.Ranh || banNguon.contains(b.getMaBan())) {
                banDichList.add(Map.of("maBan", b.getMaBan(), "tenBan", b.getTenBan()));
            }
        }
        return Map.of("success", true, "banDichList", banDichList);
    }

    // GỘP BÀN - Bước 2: Tổng hợp món từ các bàn nguồn
    @PostMapping("/gopban/tonghopmon")
    @ResponseBody
    public Object tongHopMon(@RequestParam("banNguon") String banNguonStr,
                             @RequestParam("banDich") Integer banDich,
                             @RequestParam("tenKhach") String tenKhach,
                             @RequestParam("sdtKhach") String sdtKhach,
                             @RequestParam("ngayGioDat") String ngayGioDat) {
        String[] arr = banNguonStr.split(",");
        List<Integer> banNguon = new ArrayList<>();
        for (String s : arr) {
            try { banNguon.add(Integer.parseInt(s)); } catch (Exception ignored) {}
        }
        // Lấy tất cả hóa đơn của các bàn nguồn
        List<ChiTietDatBan> allCTDB = chiTietDatBanRepository.findAll();
        List<Integer> hoaDonNguon = new ArrayList<>();
        for (Integer maBan : banNguon) {
            ChiTietDatBan ctdb = allCTDB.stream()
                .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
                .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
                .findFirst().orElse(null);
            if (ctdb != null && ctdb.getHoaDon() != null) {
                hoaDonNguon.add(ctdb.getHoaDon().getMaHoaDon());
            }
        }

        List<ChiTietHoaDon> allCTHD = chiTietHoaDonRepository.findAll();
        Map<Integer, Map<String, Object>> monMap = new HashMap<>(); // maThucDon -> {tenMon, soLuong, gia, thanhTien}
        for (ChiTietHoaDon cthd : allCTHD) {
            if (cthd.getHoaDon() != null && hoaDonNguon.contains(cthd.getHoaDon().getMaHoaDon())) {
                Integer maThucDon = cthd.getThucDon().getMaThucDon();
                String tenMon = cthd.getThucDon().getTenMon();
                Integer soLuong = cthd.getSoLuong();
                Double gia = cthd.getGiaTaiThoiDiemBan();
                Double thanhTien = cthd.getThanhTien();
                if (!monMap.containsKey(maThucDon)) {
                    monMap.put(maThucDon, new HashMap<>(Map.of(
                        "maThucDon", maThucDon,
                        "tenMon", tenMon,
                        "soLuong", soLuong,
                        "giaTaiThoiDiemBan", gia,
                        "thanhTien", thanhTien
                    )));
                } else {
                    Map<String, Object> m = monMap.get(maThucDon);
                    m.put("soLuong", (Integer)m.get("soLuong") + soLuong);
                    m.put("thanhTien", (Double)m.get("thanhTien") + thanhTien);
                }
            }
        }
        List<Map<String, Object>> monList = new ArrayList<>(monMap.values());
        return Map.of("success", true, "monList", monList);
    }

    // GỘP BÀN - Bước 3: Xác nhận gộp bàn
    @PostMapping("/gopban/xacnhan")
    @ResponseBody
    public Object xacNhanGopBan(@RequestParam("banNguon") String banNguonStr,
                                @RequestParam("banDich") Integer banDich,
                                @RequestParam("tenKhach") String tenKhach,
                                @RequestParam("sdtKhach") String sdtKhach,
                                @RequestParam("ngayGioDat") String ngayGioDat,
                                @RequestParam("maThucDon") String maThucDonStr,
                                @RequestParam("soLuong") String soLuongStr,
                                @RequestParam("gia") String giaStr,
                                @RequestParam("tongTien") Double tongTien) {
        try {
            String[] arr = banNguonStr.split(",");
            List<Integer> banNguon = new ArrayList<>();
            for (String s : arr) {
                try { banNguon.add(Integer.parseInt(s)); } catch (Exception ignored) {}
            }
            String[] arrMaThucDon = maThucDonStr.split(",");
            String[] arrSoLuong = soLuongStr.split(",");
            String[] arrGia = giaStr.split(",");
            // tạo hóa đơn mới cho bàn đích
            HoaDon hoaDon = new HoaDon();
            hoaDon.setNgayGioTao(new Date());
            hoaDon.setTrangThai(false);
            hoaDon.setTongTien(tongTien);
            hoaDon = hoaDonRepository.save(hoaDon);
            // tạo ChiTietDatBan mới cho bàn đích
            Ban ban = banRepository.findById(banDich).orElse(null);
            if (ban == null) return Map.of("success", false, "message", "Không tìm thấy bàn đích.");
            // lấy nhân viên hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            Integer maNhanVien = userDetail.getUsersDB().getMaNhanVien();
            Users nhanVien = userRepository.findById(maNhanVien).orElse(null);
            ChiTietDatBan ctdb = new ChiTietDatBan();
            ctdb.setBan(ban);
            ctdb.setHoaDon(hoaDon);
            ctdb.setNhanVien(nhanVien);
            ctdb.setTenKhachHang(tenKhach);
            ctdb.setSdtKhachHang(sdtKhach);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            ctdb.setNgayGioDat(sdf.parse(ngayGioDat));
            chiTietDatBanRepository.save(ctdb);
            // gộp các món vào ChiTietHoaDon của hóa đơn mới
            for (int i = 0; i < arrMaThucDon.length; i++) {
                Integer maTD = Integer.parseInt(arrMaThucDon[i]);
                Integer sl = Integer.parseInt(arrSoLuong[i]);
                Double g = Double.parseDouble(arrGia[i]);
                Double thanhTien = sl * g;
                ThucDon thucDon = thucDonRepository.findById(maTD).orElse(null);
                if (thucDon != null) {
                    ChiTietHoaDon cthd = new ChiTietHoaDon();
                    cthd.setHoaDon(hoaDon);
                    cthd.setThucDon(thucDon);
                    cthd.setSoLuong(sl);
                    cthd.setGiaTaiThoiDiemBan(g);
                    cthd.setThanhTien(thanhTien);
                    chiTietHoaDonRepository.save(cthd);
                }
            }
            // xóa ChiTietDatBan và ChiTietHoaDon của các bàn nguồn
            List<ChiTietDatBan> allCTDB = chiTietDatBanRepository.findAll();
            List<ChiTietHoaDon> allCTHD = chiTietHoaDonRepository.findAll();
            List<Integer> hoaDonNguon = new ArrayList<>();
            for (Integer maBan : banNguon) {
                ChiTietDatBan ctdbNguon = allCTDB.stream()
                    .filter(c -> c.getBan() != null && c.getBan().getMaBan().equals(maBan))
                    .sorted((a, b) -> b.getNgayGioDat().compareTo(a.getNgayGioDat()))
                    .findFirst().orElse(null);
                if (ctdbNguon != null && ctdbNguon.getHoaDon() != null) {
                    hoaDonNguon.add(ctdbNguon.getHoaDon().getMaHoaDon());
                    chiTietDatBanRepository.delete(ctdbNguon);
                }
            }
            for (ChiTietHoaDon cthd : allCTHD) {
                if (cthd.getHoaDon() != null && hoaDonNguon.contains(cthd.getHoaDon().getMaHoaDon())) {
                    chiTietHoaDonRepository.delete(cthd);
                }
            }
            for (Integer maBan : banNguon) {     // set trạng thái các bàn nguồn về "Rảnh", bàn đích về "Đang Dùng"
                Ban b = banRepository.findById(maBan).orElse(null);
                if (b != null) {
                    b.setTinhTrang(TinhTrangBan.Ranh);
                    banRepository.save(b);
                }
            }
            ban.setTinhTrang(TinhTrangBan.DangDung);
            banRepository.save(ban);
            return Map.of("success", true, "message", "Gộp bàn thành công!");
        } catch (Exception e) {
            return Map.of("success", false, "message", "Lỗi khi gộp bàn: " + e.getMessage());
        }
    }

    @GetMapping("/tachban/{maBan}")
    @ResponseBody
    public Object getTachBanInfo(@PathVariable Integer maBan) {
        Ban banNguon = banRepository.findById(maBan).orElse(null);
        if (banNguon == null || banNguon.getTinhTrang() != TinhTrangBan.DangDung) {
            return java.util.Map.of("success", false, "message", "Chỉ tách được bàn đang sử dụng!");
        }
        // Lấy hóa đơn đang mở của bàn
        List<HoaDon> hoaDonList = hoaDonRepository.findHoaDonDangMoByMaBan(maBan);
        if (hoaDonList.isEmpty()) {
            return Map.of("success", false, "message", "Không tìm thấy hóa đơn đang mở cho bàn này!");
        }
        HoaDon hoaDonNguon = hoaDonList.get(0);
        // Lấy danh sách món trên bàn
        List<ChiTietHoaDon> chiTietList = chiTietHoaDonRepository.findByHoaDon(hoaDonNguon);
        List<Object> monList = new ArrayList<>();
        for (ChiTietHoaDon cthd : chiTietList) {
            monList.add(Map.of(
                "maThucDon", cthd.getThucDon().getMaThucDon(),
                "tenMon", cthd.getThucDon().getTenMon(),
                "soLuong", cthd.getSoLuong(),
                "giaTaiThoiDiemBan", cthd.getGiaTaiThoiDiemBan(),
                "thanhTien", cthd.getThanhTien()
            ));
        }
        // Lấy danh sách bàn rảnh
        List<Ban> banRanhList = banRepository.findByTinhTrang(TinhTrangBan.Ranh);
        List<Object> banRanh = new ArrayList<>();
        for (Ban b : banRanhList) {
            banRanh.add(Map.of("maBan", b.getMaBan(), "tenBan", b.getTenBan()));
        }
        return Map.of(
            "success", true,
            "banNguon", java.util.Map.of("maBan", banNguon.getMaBan(), "tenBan", banNguon.getTenBan()),
            "hoaDonNguon", hoaDonNguon.getMaHoaDon(),
            "monList", monList,
            "banRanh", banRanh
        );
    }

    @PostMapping("/tachban")
    @ResponseBody
    public Object postTachBan(
            @RequestParam Integer maBanNguon,
            @RequestParam Integer maBanDich,
            @RequestParam Integer hoaDonNguon,
            @RequestParam String monListJson,
            @RequestParam(required = false) String tenKhachHang,
            @RequestParam(required = false) String sdtKhachHang,
            @RequestParam(required = false) String ngayGioDat
    ) {
        try {
            Ban banNguon = banRepository.findById(maBanNguon).orElse(null);
            Ban banDich = banRepository.findById(maBanDich).orElse(null);
            if (banNguon == null || banDich == null) {
                return java.util.Map.of("success", false, "message", "Không tìm thấy bàn nguồn hoặc bàn đích!");
            }
            HoaDon hoaDonNguonObj = hoaDonRepository.findById(hoaDonNguon).orElse(null);
            if (hoaDonNguonObj == null) {
                return java.util.Map.of("success", false, "message", "Không tìm thấy hóa đơn nguồn!");
            }
            // Parse monListJson: [{maThucDon, soLuong, giaTaiThoiDiemBan}]
            ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            List<Map<String, Object>> monList = mapper.readValue(monListJson, java.util.List.class);
            // Tạo hóa đơn mới cho bàn đích
            HoaDon hoaDonDich = new HoaDon();
            hoaDonDich.setNgayGioTao(new java.util.Date());
            hoaDonDich.setTrangThai(false);
            hoaDonDich = hoaDonRepository.save(hoaDonDich);
            // Tạo ChiTietDatBan cho bàn đích
            ChiTietDatBan chiTietDatBan = new ChiTietDatBan();
            chiTietDatBan.setBan(banDich);
            chiTietDatBan.setHoaDon(hoaDonDich);
            // Lấy nhân viên hiện tại (nếu có)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            Users nhanVien = userRepository.findById(userDetail.getUsersDB().getMaNhanVien()).orElse(null);
            chiTietDatBan.setNhanVien(nhanVien);
            chiTietDatBan.setTenKhachHang(tenKhachHang != null ? tenKhachHang : "");
            chiTietDatBan.setSdtKhachHang(sdtKhachHang != null ? sdtKhachHang : "");
            if (ngayGioDat != null) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date ngayGio = sdf.parse(ngayGioDat);
                    chiTietDatBan.setNgayGioDat(ngayGio);
                } catch (Exception e) {
                    chiTietDatBan.setNgayGioDat(new java.util.Date());
                }
            } else {
                chiTietDatBan.setNgayGioDat(new java.util.Date());
            }
            chiTietDatBanRepository.save(chiTietDatBan);
            double tongTien = 0;
            // Xử lý từng món tách
            for (java.util.Map<String, Object> mon : monList) {
                Integer maThucDon = Integer.parseInt(mon.get("maThucDon").toString());
                Integer soLuongTach = Integer.parseInt(mon.get("soLuong").toString());
                Double giaTaiThoiDiemBan = Double.parseDouble(mon.get("giaTaiThoiDiemBan").toString());
                // Lấy ThucDon
                com.vn.model.ThucDon thucDon = thucDonRepository.findById(maThucDon).orElse(null);
                if (thucDon == null || soLuongTach <= 0) continue;
                // Trừ số lượng ở bàn nguồn
                ChiTietHoaDon cthdNguon = chiTietHoaDonRepository.findByHoaDonAndThucDon(hoaDonNguonObj, thucDon);
                if (cthdNguon == null || cthdNguon.getSoLuong() < soLuongTach) {
                    return java.util.Map.of("success", false, "message", "Số lượng tách vượt quá số lượng hiện có!");
                }
                cthdNguon.setSoLuong(cthdNguon.getSoLuong() - soLuongTach);
                cthdNguon.setThanhTien(cthdNguon.getSoLuong() * cthdNguon.getGiaTaiThoiDiemBan());
                if (cthdNguon.getSoLuong() == 0) {
                    chiTietHoaDonRepository.delete(cthdNguon);
                } else {
                    chiTietHoaDonRepository.save(cthdNguon);
                }
                // Thêm vào bàn đích
                ChiTietHoaDon cthdDich = new ChiTietHoaDon();
                cthdDich.setHoaDon(hoaDonDich);
                cthdDich.setThucDon(thucDon);
                cthdDich.setSoLuong(soLuongTach);
                cthdDich.setGiaTaiThoiDiemBan(giaTaiThoiDiemBan);
                cthdDich.setThanhTien(soLuongTach * giaTaiThoiDiemBan);
                chiTietHoaDonRepository.save(cthdDich);
                tongTien += soLuongTach * giaTaiThoiDiemBan;
            }
            // Cập nhật tổng tiền hóa đơn đích
            hoaDonDich.setTongTien(tongTien);
            hoaDonRepository.save(hoaDonDich);
            banDich.setTinhTrang(TinhTrangBan.DangDung);
            banRepository.save(banDich);
            List<ChiTietHoaDon> conLai = chiTietHoaDonRepository.findByHoaDon(hoaDonNguonObj);
            if (conLai.isEmpty()) {
                banNguon.setTinhTrang(TinhTrangBan.Ranh);
                hoaDonNguonObj.setTrangThai(true); // đóng hóa đơn cũ
                hoaDonRepository.save(hoaDonNguonObj);
            }
            banRepository.save(banNguon);
            return Map.of("success", true, "message", "Tách bàn thành công!");
        } catch (Exception e) {
            return Map.of("success", false, "message", "Lỗi xử lý: " + e.getMessage());
        }
    }

}

   
