package com.vn.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.vn.auth.CustomUserDetail;
import java.text.SimpleDateFormat;
import com.vn.model.DonViTinh;
import com.vn.model.DonXuat;
import com.vn.model.HangHoa;
import com.vn.model.Users;
import com.vn.model.DonNhap;
import com.vn.repository.DonViTinhRepository;
import com.vn.repository.HangHoaRepository;
import com.vn.repository.DonNhapRepository;
import com.vn.repository.DonXuatRepository;
import jakarta.validation.Valid;

@Controller
public class HangHoaController {

    @Autowired
    private HangHoaRepository hangHoaRepository;

    @Autowired
    private DonViTinhRepository donViTinhRepository;

    @Autowired
    private DonNhapRepository donNhapRepository;

    @Autowired
    private DonXuatRepository donXuatRepository;

    // xuathang 
    @GetMapping("/admin/hanghoa/hanghoa-xuathang")
    public String showCreateFormXuatHang(Model model) {
        model.addAttribute("xuatHang", new DonXuat());
        List<HangHoa> hangHoaList = hangHoaRepository.findAll();
        model.addAttribute("hangHoaList", hangHoaList);
        return "admin/hanghoa/hanghoa-xuathang";
    }

    @PostMapping("/admin/hanghoa/hanghoa-xuathang")
    public String xuatHang(@ModelAttribute("xuatHang") DonXuat donXuat, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        donXuat.setNhanVien(userDetail.getUsersDB());
        HangHoa hangHoa = hangHoaRepository.findById(donXuat.getHangHoa().getMaHangHoa()).orElse(null);
        if (hangHoa == null) {
            model.addAttribute("error", "Không tìm thấy hàng hóa!");
            return "admin/hanghoa/hanghoa-xuathang";
        }
        if (donXuat.getSoLuong() > hangHoa.getSoLuong()) {
            model.addAttribute("error", "Số lượng xuất vượt quá số lượng tồn!");
            List<HangHoa> hangHoaList = hangHoaRepository.findAll();
            model.addAttribute("hangHoaList", hangHoaList);
            return "admin/hanghoa/hanghoa-xuathang";
        }
        hangHoa.setSoLuong(hangHoa.getSoLuong() - donXuat.getSoLuong());
        hangHoaRepository.save(hangHoa);
        donXuat.setHangHoa(hangHoa);
        donXuatRepository.save(donXuat);
        redirectAttributes.addFlashAttribute("successMessage", "Xuất hàng thành công!");
        return "redirect:/admin/hanghoa/hanghoa-danhsachxuat";
    }

    @GetMapping("/admin/hanghoa/hanghoa-danhsachxuat")
    public String danhSachXuatHang( @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(defaultValue = "") String keyword,
                              Model model) {
        Page<DonXuat> posts = donXuatRepository.searchDonXuatByHangHoa(keyword, PageRequest.of(page, size));
        int totalPages = posts.getTotalPages();

        if (totalPages > 0) {
            int start = Math.max(1, page - 1);
            int end = Math.min(totalPages, page + 3 );

            if (end - start < 4) {
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

        List<Integer> pageNums = new ArrayList();
        for (int i = 1; i <= posts.getTotalPages(); i++) {
            pageNums.add(i);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        return "/admin/hanghoa/hanghoa-danhsachxuat";
    }

    // hang hoahoa
    @GetMapping("/admin/hanghoa/hanghoa-create")
    public String showCreateHangHoaForm(Model model) {
        model.addAttribute("hangHoa", new HangHoa());
        List<DonViTinh> donViTinhList = donViTinhRepository.findAll();
        model.addAttribute("donViTinhList", donViTinhList);
        return "admin/hanghoa/hanghoa-create";
    }


    

    @PostMapping("/admin/hanghoa/hanghoa-create")
    public String createHangHoa(@Valid @ModelAttribute("hangHoa") HangHoa hangHoa, BindingResult result,
                                 Model model, RedirectAttributes redirectAttributes
                                ) {

        if (hangHoaRepository.existsByMaHangHoa(hangHoa.getMaHangHoa())) {
            result.rejectValue("maHangHoa", null, "Mã hàng hóa đã tồn tại");
        }

        if(hangHoaRepository.existsByTenHangHoa(hangHoa.getTenHangHoa())) {
            result.rejectValue("tenHangHoa", null, "Tên hàng hóa đã tồn tại");

        }

             if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("hangHoa", hangHoa);
            model.addAttribute("donViTinhList", donViTinhRepository.findAll());
            return "admin/hanghoa/hanghoa-create";
        }
     


        hangHoaRepository.save(hangHoa);
        DonViTinh donViTinh = donViTinhRepository.findByMaDonViTinh(hangHoa.getDonViTinh().getMaDonViTinh());
        hangHoa.setDonViTinh(donViTinh);
        hangHoaRepository.save(hangHoa);
        redirectAttributes.addFlashAttribute("success", "Thêm hàng hóa thành công!");
        return "redirect:/admin/hanghoa/hanghoa-list";
    }

     @GetMapping("/admin/hanghoa/hanghoa-list")
    public String listCustomer( @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "2") int size,
                              @RequestParam(defaultValue = "") String keyword,
                              Model model) {
        Page<HangHoa> posts = hangHoaRepository.searchHangHoa(keyword, PageRequest.of(page, size));

        int totalPages = posts.getTotalPages();

        if (totalPages > 0) {
            int start = Math.max(1, page - 1);
            int end = Math.min(totalPages, page + 3 );

            if (end - start < 4) {
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

        List<Integer> pageNums = new ArrayList();
        for (int i = 1; i <= posts.getTotalPages(); i++) {
            pageNums.add(i);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        return "admin/hanghoa/hanghoa-list";
    }


    // nhap hang
    @GetMapping("/hanghoa/nhapHangHoa/{maHangHoa}")
    @ResponseBody
    public Object getHangHoaNhap(@PathVariable Integer maHangHoa) {
        HangHoa hangHoa = hangHoaRepository.findById(maHangHoa).orElse(null);
        if (hangHoa == null) {
            return Map.of("success", false, "message", "Không tìm thấy hàng hóa!");
        }
        return Map.of(
            "success", true,
            "maHangHoa", hangHoa.getMaHangHoa(),
            "tenHangHoa", hangHoa.getTenHangHoa(),
            "donViTinh", hangHoa.getDonViTinh().getTenDonVi()
        );
    }

    @PostMapping("/hanghoa/nhap")
    @ResponseBody
    public Object nhapHangHoa(
            @RequestParam Integer maHangHoa,
            @RequestParam String ngayNhap,
            @RequestParam Integer soLuong,
            @RequestParam Double tongTien
    ) {
        try {
            HangHoa hangHoa = hangHoaRepository.findById(maHangHoa).orElse(null);
            if (hangHoa == null) {
                return java.util.Map.of("success", false, "message", "Không tìm thấy hàng hóa!");
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
            Users nhanVien = userDetail.getUsersDB();
            String maDonNhap = "DN" + System.currentTimeMillis();
            DonNhap donNhap = new DonNhap();
            donNhap.setMaDonNhap(maDonNhap);
            donNhap.setHangHoa(hangHoa);
            donNhap.setNhanVien(nhanVien);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            donNhap.setNgayNhap(sdf.parse(ngayNhap));
            donNhap.setSoLuong(soLuong);
            donNhap.setTongTien(tongTien);
            donNhapRepository.save(donNhap);
            int soLuongMoi = hangHoa.getSoLuong() + soLuong;
            hangHoa.setSoLuong(soLuongMoi);
            double donGiaMoi = soLuongMoi > 0 ? (tongTien / soLuong) : hangHoa.getDonGia();
            hangHoa.setDonGia(donGiaMoi);
            hangHoaRepository.save(hangHoa);
            return java.util.Map.of(
                "success", true,
                "message", "Nhập hàng thành công!",
                "donNhap", java.util.Map.of(
                    "maDonNhap", donNhap.getMaDonNhap(),
                    "tenHangHoa", hangHoa.getTenHangHoa(),
                    "maHangHoa", hangHoa.getMaHangHoa(),
                    "ngayNhap", sdf.format(donNhap.getNgayNhap()),
                    "soLuong", donNhap.getSoLuong(),
                    "tongTien", donNhap.getTongTien(),
                    "donViTinh", hangHoa.getDonViTinh().getTenDonVi(),
                    "donGia", hangHoa.getDonGia(),
                    "nhanVien", nhanVien.getHoTen()
                )
            );
        } catch (Exception e) {
            return Map.of("success", false, "message", "Lỗi: " + e.getMessage());
        }
    }

    @GetMapping("/admin/hanghoa/hanghoa-danhsachnhap")
    public String viewDanhSachNhapHang() {
    return "admin/hanghoa/hanghoa-danhsachnhap";
    }

    @GetMapping("/hanghoa/danhsachnhap")
    @ResponseBody
    public Object getDanhSachDonNhap() {
        List<DonNhap> list = donNhapRepository.findAll();
        List<Object> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (DonNhap dn : list) {
            result.add(Map.of(
                "maDonNhap", dn.getMaDonNhap(),
                "tenHangHoa", dn.getHangHoa().getTenHangHoa(),
                "maHangHoa", dn.getHangHoa().getMaHangHoa(),
                "ngayNhap", sdf.format(dn.getNgayNhap()),
                "soLuong", dn.getSoLuong(),
                "tongTien", dn.getTongTien(),
                "donViTinh", dn.getHangHoa().getDonViTinh().getTenDonVi(),
                "donGia", dn.getHangHoa().getDonGia(),
                "nhanVien", dn.getNhanVien().getHoTen()
            ));
        }
        return Map.of("success", true, "list", result);
    }

    @PostMapping("/admin/hanghoa/delete")
    public String deleteHangHoa(@RequestParam("hangHoaIds") List<Integer> hangHoaIds, Model model) {
        List<HangHoa> hangHoas = hangHoaRepository.findAllById(hangHoaIds);
        for (HangHoa hangHoa : hangHoas) {
            hangHoa.setDeleted(true);
            hangHoaRepository.save(hangHoa);
        }
        model.addAttribute("message", "Đã xóa thành công các hàng hóa đã chọn.");
        return "redirect:/admin/hanghoa/hanghoa-list";
    }

}
