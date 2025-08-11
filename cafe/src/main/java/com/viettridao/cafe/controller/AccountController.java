package com.viettridao.cafe.controller; // Khai báo gói cho lớp AccountController

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest; // Nhập lớp UpdateAccountRequest từ gói DTO
import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.service.AccountService; // Nhập giao diện AccountService từ gói service
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // Nhập chú thích RequiredArgsConstructor của Lombok để tạo constructor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller; // Nhập chú thích Controller của Spring để đánh dấu lớp là một controller
import org.springframework.ui.Model; // Nhập lớp Model của Spring để truyền dữ liệu đến view
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping; // Nhập chú thích GetMapping để xử lý yêu cầu GET
import org.springframework.web.bind.annotation.ModelAttribute; // Nhập chú thích ModelAttribute để liên kết tham số với đối tượng model
import org.springframework.web.bind.annotation.PostMapping; // Nhập chú thích PostMapping để xử lý yêu cầu POST
import org.springframework.web.bind.annotation.RequestMapping; // Nhập chú thích RequestMapping để ánh xạ các yêu cầu web
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Nhập lớp RedirectAttributes để truyền thuộc tính flash sau khi chuyển hướng

/**
 * AccountController
 *
 * Lớp này xử lý các yêu cầu liên quan đến quản lý tài khoản người dùng,
 * bao gồm hiển thị thông tin tài khoản và cập nhật thông tin cá nhân.
 *
 * Phiên bản 1.0
 *
 * Ngày: 2025-07-23
 *
 * Bản quyền (c) 2025 VietTriDao. Đã đăng ký bản quyền.
 *
 * Nhật ký sửa đổi:
 * NGÀY                 TÁC GIẢ          MÔ TẢ
 * -----------------------------------------------------------------------
 * 2025-07-23           Hoa Mãn Lâu     Tạo và định dạng ban đầu.
 */
@Controller // Đánh dấu lớp này là một Spring MVC Controller
@RequiredArgsConstructor // Tự động tạo constructor với các trường final
@RequestMapping("/account") // Ánh xạ tất cả các yêu cầu bắt đầu bằng "/account" đến controller này
public class AccountController {

    /**
     * Tiêm phụ thuộc AccountService để xử lý logic liên quan đến tài khoản.
     */
    private final AccountService accountService; // Khai báo một trường final cho AccountService, được tiêm qua constructor

    /**
     * Hiển thị trang thông tin tài khoản.
     * Xử lý yêu cầu GET đến "/account".
     *
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) cho trang tài khoản.
     */


    @GetMapping("")
    public String showAccountInfo(Model model) {
        // Lấy username hiện tại từ security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        // Lấy thông tin tài khoản từ service
        AccountEntity accountEntity = accountService.getAccountByUsername(currentUsername);

        // Đưa dữ liệu tài khoản ra view
        model.addAttribute("account", accountEntity != null ? accountEntity : new AccountEntity());

        return "/accounts/account";
    }

    /**
     * Xử lý yêu cầu cập nhật thông tin tài khoản cá nhân.
     * Xử lý yêu cầu POST đến "/account/update".
     *
     * @param request Đối tượng UpdateAccountRequest chứa thông tin tài khoản cần cập nhật.
     * @param redirectAttributes Đối tượng RedirectAttributes để thêm các thuộc tính flash
     * (ví dụ: thông báo thành công/lỗi) cho yêu cầu chuyển hướng.
     * @return Chuỗi chuyển hướng đến trang tài khoản sau khi cập nhật.
     */
    @PostMapping("/update") // Ánh xạ yêu cầu POST đến "/account/update"
    public String accountUpdate(@Valid @ModelAttribute("account") UpdateAccountRequest request, RedirectAttributes redirectAttributes, Model model) { // Định nghĩa phương thức để cập nhật tài khoản
        try { // Bắt đầu khối try để xử lý ngoại lệ
            accountService.updateAccount(request); // Gọi dịch vụ để cập nhật tài khoản với dữ liệu từ request

            // 2. Lấy lại tài khoản vừa cập nhật (bạn nên lấy theo username đăng nhập hiện tại)
            Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
            String username = currentAuth.getName();
            AccountEntity updatedAccount = accountService.getAccountByUsername(username);

            // 3. Cập nhật lại principal trong SecurityContext
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    updatedAccount, // principal mới
                    currentAuth.getCredentials(),
                    updatedAccount.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);



            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!"); // Thêm thông báo thành công vào flash attributes
        } catch (Exception e) { // Bắt các ngoại lệ có thể xảy ra
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage()); // Thêm thông báo lỗi vào flash attributes
        }

        return "redirect:/account";// Chuyển hướng người dùng trở lại trang "/account"
    }

}
