package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.AddNhanVienRequest;
import com.viettridao.cafe.dto.request.UpdateNhanVienRequest;
import com.viettridao.cafe.dto.response.NhanVienResponse;
import com.viettridao.cafe.model.ChucVu;
import com.viettridao.cafe.model.NhanVien;
import com.viettridao.cafe.service.ChucVuService;
import com.viettridao.cafe.service.NhanVienService;
import com.viettridao.cafe.service.TaiKhoanService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController extends BaseController {

    private final NhanVienService nhanVienService;
    private final ChucVuService chucVuService;
    private final TaiKhoanService taiKhoanService;
    // ============= DASHBOARD =============

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "home");
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm dữ liệu cho dashboard
        model.addAttribute("totalOrders", 150);
        model.addAttribute("totalRevenue", 25000000);
        model.addAttribute("totalCustomers", 45);
        model.addAttribute("todayOrders", 12);

        return "home";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "profile");
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm thông tin profile (thay thế bằng dữ liệu thực)
        model.addAttribute("fullName", "Nguyễn Văn Admin");
        model.addAttribute("email", "admin@cafe.com");
        model.addAttribute("phone", "0123456789");
        model.addAttribute("position", "Quản lý");

        return "home";
    }

    // ========== EMPLOYEE MANAGEMENT ENDPOINTS ==========

    @GetMapping("/employees")
    public String employeesList(Model model, HttpSession session,
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "employees");
        model.addAttribute("employeeAction", "list");
        model.addAttribute("username", session.getAttribute("username"));

        try {
            // Lấy danh sách nhân viên từ database
            List<NhanVienResponse> employees = nhanVienService.getListNhanVien();

            model.addAttribute("employees", employees);

            // Success/error messages
            if (success != null) {
                switch (success) {
                    case "add" -> model.addAttribute("successMessage", "Thêm nhân viên thành công!");
                    case "edit" -> model.addAttribute("successMessage", "Cập nhật nhân viên thành công!");
                    case "delete" -> model.addAttribute("successMessage", "Xóa nhân viên thành công!");
                }
            }
            if (error != null) {
                model.addAttribute("errorMessage", "Có lỗi xảy ra: " + error);
            }

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Không thể tải danh sách nhân viên: " + e.getMessage());
            model.addAttribute("totalEmployees", 0);
            model.addAttribute("activeEmployees", 0);
            model.addAttribute("inactiveEmployees", 0);
            model.addAttribute("employees", List.of()); // Empty list để tránh lỗi template
        }

        return "home";
    }

    @GetMapping("/employees/add")
    public String employeesAdd(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        List<ChucVu> listChucVu = chucVuService.getListChucVu();
        model.addAttribute("listChucVu", listChucVu);
        model.addAttribute("activeTab", "employees");
        model.addAttribute("employeeAction", "add");
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm object để bind form
        if (!model.containsAttribute("addNhanVienRequest")) {
            model.addAttribute("addNhanVienRequest", new AddNhanVienRequest());
        }

        return "home";
    }

    @GetMapping("/employees/edit")
    public String employeesEdit(Model model, HttpSession session,
            @RequestParam(value = "id", required = false) Integer id) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        // Kiểm tra ID có được truyền không
        if (id == null) {
            return "redirect:/employees?error=missing-id";
        }

        try {
            // Lấy thông tin nhân viên theo ID
            NhanVien employee = nhanVienService.getNhanVienById(id);
            System.out.println("Editing employee: " + employee.getHoTen() + " (ID: " + id + ")");

            // Lấy danh sách chức vụ để hiển thị trong dropdown
            List<ChucVu> listChucVu = chucVuService.getListChucVu();

            model.addAttribute("employee", employee);
            model.addAttribute("listChucVu", listChucVu);
            model.addAttribute("activeTab", "employees");
            model.addAttribute("employeeAction", "edit");
            model.addAttribute("username", session.getAttribute("username"));

            return "home";
        } catch (Exception e) {
            System.err.println("Error loading employee: " + e.getMessage());
            return "redirect:/employees?error=employee-not-found";
        }
    }

    @GetMapping("/employees/delete")
    public String employeesDelete(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "employees");
        model.addAttribute("employeeAction", "delete");
        model.addAttribute("username", session.getAttribute("username"));

        return "home";
    }

    // ========== POST ENDPOINTS FOR EMPLOYEE ACTIONS ==========

    @PostMapping("/employees/add")
    public String employeesAddPost(@ModelAttribute AddNhanVienRequest request,
            Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        try {
            // Validate dữ liệu cơ bản
            if (request.getHoTen() == null || request.getHoTen().trim().isEmpty()) {
                throw new RuntimeException("Họ tên không được để trống!");
            }

            if (request.getSoDienThoai() == null || request.getSoDienThoai().trim().isEmpty()) {
                throw new RuntimeException("Số điện thoại không được để trống!");
            }

            if (request.getMaChucVu() == null) {
                throw new RuntimeException("Vui lòng chọn chức vụ!");
            }

            if (request.getLuong() == null || request.getLuong() <= 0) {
                throw new RuntimeException("Lương phải lớn hơn 0!");
            }

            // Thêm nhân viên
            nhanVienService.addNhanVien(request);
            System.out.println("---------------------Thêm nhân viên thành công----------------: " + request.getHoTen());
            return "redirect:/employees?success=add";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("addNhanVienRequest", request);
            return employeesAdd(model, session);
        }
    }

    @PostMapping("/employees/edit/{id}")
    public String employeesEditPost(@PathVariable Integer id,
            @RequestParam String hoTen,
            @RequestParam(required = false) String diaChi,
            @RequestParam Integer maChucVu,
            @RequestParam String soDienThoai,
            @RequestParam(required = false) String tenDangNhap,
            @RequestParam(required = false) String matKhauMoi,
            Model model, HttpSession session) {

        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        try {
            // Tạo request object để update nhân viên
            UpdateNhanVienRequest request = new UpdateNhanVienRequest();
            request.setMaNhanVien(id);
            request.setHoTen(hoTen);
            request.setDiaChi(diaChi);
            request.setMaChucVu(maChucVu);
            request.setSoDienThoai(soDienThoai);
            request.setTenDangNhap(tenDangNhap);
            request.setMatKhauMoi(matKhauMoi);

            // Gọi service để update
            nhanVienService.updateNhanVien(request);

            System.out.println("Successfully updated employee with ID: " + id);
            return "redirect:/employees?success=edit";

        } catch (Exception e) {
            System.err.println("Error updating employee: " + e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "redirect:/employees/edit?id=" + id + "&error=" + e.getMessage();
        }
    }

    @PostMapping("/employees/lock/{id}")
    public String lockEmployee(@PathVariable Integer id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        try {
            System.out.println("Attempting to lock employee with ID: " + id);
            nhanVienService.lockEmployee(id);
            redirectAttributes.addFlashAttribute("successMessage", "xóa nhân viên thành công!");
            System.out.println("Successfully locked employee with ID: " + id);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi xóa nhân viên: " + e.getMessage());
            System.err.println("Error locking employee: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/employees";
    }

    // ========== OTHER MODULE ENDPOINTS ==========

    @GetMapping("/sales")
    public String sales(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "sales");
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm dữ liệu bán hàng
        model.addAttribute("todaySales", 2500000);
        model.addAttribute("todayOrders", 25);
        model.addAttribute("avgOrderValue", 100000);

        return "home";
    }

    @GetMapping("/inventory")
    public String inventory(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "inventory");
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm dữ liệu kho hàng
        model.addAttribute("totalProducts", 50);
        model.addAttribute("lowStockItems", 5);
        model.addAttribute("outOfStockItems", 2);

        return "home";
    }

    @GetMapping("/marketing")
    public String marketing(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "marketing");
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm dữ liệu marketing
        model.addAttribute("activeCampaigns", 3);
        model.addAttribute("totalCustomers", 1200);
        model.addAttribute("loyaltyMembers", 800);

        return "home";
    }

    @GetMapping("/reports")
    public String reports(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "reports");
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm dữ liệu báo cáo
        model.addAttribute("monthlyRevenue", 75000000);
        model.addAttribute("monthlyOrders", 450);
        model.addAttribute("growthRate", 15.5);

        return "home";
    }

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "about");
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm thông tin về ứng dụng
        model.addAttribute("appVersion", "1.0.0");
        model.addAttribute("buildDate", "2025-06-18");
        model.addAttribute("developer", "Việt Trí Đào");

        return "home";
    }
}
