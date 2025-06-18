package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.LoginRequest;
import com.viettridao.cafe.dto.response.ThongTinDangNhapResponse;
import com.viettridao.cafe.service.TaiKhoanService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    @GetMapping("/")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@ModelAttribute LoginRequest loginRequest,
            Model model, HttpSession session) {
        try {
            ThongTinDangNhapResponse thongTinUser = taiKhoanService.login(loginRequest);

            if (thongTinUser != null && thongTinUser.getMatKhau().equals(loginRequest.getPassword())) {
                // Lưu thông tin user vào session
                session.setAttribute("userInfo", thongTinUser);
                session.setAttribute("isLoggedIn", true);
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Sai tài khoản hoặc mật khẩu");
                model.addAttribute("loginRequest", loginRequest);
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Tài khoản của bạn đã bị khóa");
            model.addAttribute("loginRequest", loginRequest);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // Kiểm tra authentication cho tất cả các endpoint
    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("isLoggedIn") != null && (Boolean) session.getAttribute("isLoggedIn");
    }

    // ============= CÁC ENDPOINT CHO TỪNG TAB =============

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

    @GetMapping("/employees")
    public String employees(Model model, HttpSession session,
            @RequestParam(value = "action", defaultValue = "list") String action) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }

        model.addAttribute("activeTab", "employees");
        model.addAttribute("employeeAction", action);
        model.addAttribute("username", session.getAttribute("username"));

        // Thêm dữ liệu nhân viên
        model.addAttribute("totalEmployees", 15);
        model.addAttribute("activeEmployees", 12);
        model.addAttribute("onLeaveEmployees", 3);

        return "home";
    }

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
