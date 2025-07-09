package com.viettridao.cafe.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.auth.LoginRequest;
import com.viettridao.cafe.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller xử lý authentication và authorization cho ứng dụng.
 * 
 * Quyết định thiết kế:
 * - Sử dụng Spring Security cho core authentication
 * - Controller chỉ handle UI presentation layer
 * - AuthService xử lý business logic liên quan đến auth
 * 
 * Security considerations:
 * - Login form được bảo vệ CSRF token
 * - Redirect safety để tránh open redirect attacks
 * - Session management được Spring Security handle
 * 
 * Flow: Login form → Spring Security filter → Success redirect → /home
 * 
 * @author Cafe Management Team
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController {

    // AuthService để xử lý business logic authentication nếu cần
    private final AuthService authService;

    /**
     * Hiển thị form đăng nhập cho user.
     * 
     * Endpoint này chỉ render login form, việc xử lý authentication
     * thực tế được Spring Security handle thông qua security config.
     * 
     * Design pattern: Separation of concerns
     * - Controller: UI presentation
     * - Spring Security: Authentication logic
     * - AuthService: Custom business rules nếu cần
     * 
     * @return đường dẫn tới login template
     */
    @GetMapping("")
    public String showLoginForm(Model model) {
        model.addAttribute("login", new LoginRequest());
        // Trả về login form template
        return "auth/login";
    }

    /**
     * Xử lý đăng nhập cho user.
     * 
     * Phương thức này sẽ được gọi khi form đăng nhập được submit.
     * Nó sẽ thực hiện việc xác thực thông tin đăng nhập của user
     * thông qua AuthService. Nếu thành công, user sẽ được chuyển
     * hướng tới trang chủ. Nếu thất bại, thông báo lỗi sẽ được hiển thị
     * trên trang đăng nhập.
     * 
     * @param request            chứa thông tin đăng nhập của user
     * @param bindingResult      để kiểm tra lỗi validation
     * @param redirectAttributes để truyền thông báo giữa các request
     * @return đường dẫn tới trang chủ hoặc về lại trang đăng nhập
     */
    @PostMapping("")
    public String login(@Valid @ModelAttribute("login") LoginRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                // Trả về lại trang login với lỗi validation
                return "auth/login";
            }
            boolean result = authService.login(request.getUsername(), request.getPassword());
            if (result) {
                redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
                return "redirect:/home";
            }
        } catch (IllegalArgumentException e) {
            // Lỗi thiếu thông tin đầu vào
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        } catch (AuthenticationException e) {
            // Lỗi xác thực (sai tài khoản, mật khẩu, user không tồn tại)
            redirectAttributes.addFlashAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
            return "redirect:/login";
        } catch (Exception e) {
            // Lỗi không xác định
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi, vui lòng thử lại sau");
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("error", "Đăng nhập thất bại");
        return "redirect:/login";
    }
}
