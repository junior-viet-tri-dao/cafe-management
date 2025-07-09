package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.mapper.AccountMapper;
import com.viettridao.cafe.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý tài khoản nhân viên cho các thao tác tự phục vụ.
 * 
 * Quyết định thiết kế:
 * - Sử dụng Spring Security context cho xác thực (quản lý session stateless)
 * - Format tiền tệ được thực hiện ở tầng presentation để tránh phức tạp lưu trữ
 * DB
 * - Tuân thủ pattern redirect-after-POST để ngăn submit form trùng lặp
 * 
 * Cân nhắc về hiệu suất:
 * - Một query DB duy nhất mỗi request thông qua username lookup
 * - Lazy evaluation cho format lương chỉ khi cần thiết
 * 
 * @author Cafe Management Team
 * @since 1.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    // Dependencies được inject - ưu tiên final fields để đảm bảo immutability
    private final AccountService accountService;
    private final AccountMapper accountMapper;

    /**
     * Render trang thông tin tài khoản cho user đã xác thực.
     * 
     * Bảo mật: Dựa vào authentication context của Spring Security - giả định user
     * đã được xác thực tại thời điểm này (được enforce bởi cấu hình security).
     * 
     * Hiệu suất: Một lần hit database qua username lookup. Cân nhắc caching nếu đây
     * trở thành endpoint có traffic cao.
     * 
     * @param model Spring MVC model để binding với view
     * @return đường dẫn view cho template thông tin tài khoản
     */
    @GetMapping("")
    public String showAccountInfo(Model model) {
        // Lấy username hiện tại từ security context
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        // Lấy thông tin tài khoản từ service
        AccountResponse accountResponse = accountMapper.toAccountResponse(
                accountService.getAccountByUsername(currentUsername));

        // Đưa dữ liệu tài khoản ra view
        model.addAttribute("account", accountResponse != null ? accountResponse : new AccountResponse());

        return "/accounts/account";
    }

    /**
     * Xử lý cập nhật thông tin tài khoản qua POST request.
     * 
     * Design pattern: POST-redirect-GET để ngăn duplicate form submissions
     * khi browser refresh. Flash attributes đảm bảo messages tồn tại qua redirect.
     * 
     * Error handling: Catch tất cả exceptions để cung cấp error messages thân thiện
     * với user.
     * Trong production, cân nhắc log actual exceptions để debug trong khi hiển thị
     * sanitized messages cho users.
     * 
     * @param request            DTO chứa thông tin tài khoản cập nhật từ form
     * @param redirectAttributes lưu trữ tạm thời cho success/error messages
     * @return redirect URL để ngăn form resubmission
     */
    @PostMapping("/update")
    public String updateAccountInfo(@Valid @ModelAttribute("account") UpdateAccountRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {
        // Nếu có lỗi validate, trả về lại view và truyền lỗi xuống frontend
        if (bindingResult.hasErrors()) {
            model.addAttribute("account", request);
            return "/accounts/account";
        }
        try {
            // Gọi service cập nhật tài khoản
            accountService.updateAccount(request);

            // Thông báo thành công
            redirectAttributes.addFlashAttribute("success",
                    "Cập nhật thông tin cá nhân thành công!");

        } catch (Exception e) {
            // Thông báo lỗi
            redirectAttributes.addFlashAttribute("error",
                    "Cập nhật thất bại: " + e.getMessage());
        }

        // Redirect về trang tài khoản
        return "redirect:/account";
    }
}
