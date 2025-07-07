package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.mapper.AccountMapper;
import com.viettridao.cafe.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.util.Locale;

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
     * đã được
     * xác thực tại thời điểm này (được enforce bởi cấu hình security).
     * 
     * Hiệu suất: Một lần hit database qua username lookup. Cân nhắc caching nếu đây
     * trở thành
     * endpoint có traffic cao.
     * 
     * @param model Spring MVC model để binding với view
     * @return đường dẫn view cho template thông tin tài khoản
     */
    @GetMapping("")
    public String showAccountInfo(Model model) {
        // Trích xuất username từ security context - đảm bảo non-null
        // nếu cấu hình security của chúng ta đúng
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        // TODO: Cân nhắc thêm error handling cho trường hợp user not found
        // Hiện tại giả định AccountService sẽ xử lý điều này một cách graceful
        AccountResponse accountResponse = accountMapper.toAccountResponse(
                accountService.getAccountByUsername(currentUsername));

        // Format tiền tệ ở tầng presentation để duy trì separation of concerns
        // Thay thế: Có thể làm trong mapper, nhưng cách này giữ business logic riêng
        // biệt
        formatSalaryToVND(accountResponse);

        // Defensive programming: đảm bảo view luôn có object hợp lệ để làm việc
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
    public String updateAccountInfo(@ModelAttribute("account") UpdateAccountRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            // Ủy thác business logic cho service layer - controller chỉ xử lý
            // request/response
            accountService.updateAccount(request);

            // Flash attributes tồn tại qua redirect và được consume bởi request tiếp theo
            redirectAttributes.addFlashAttribute("success",
                    "Cập nhật thông tin cá nhân thành công!");

        } catch (Exception e) {
            // TODO: Thêm proper logging ở đây trong production
            // Cân nhắc tạo custom exceptions cho các failure scenarios khác nhau
            redirectAttributes.addFlashAttribute("error",
                    "Cập nhật thất bại: " + e.getMessage());
        }

        // Redirect ngăn form resubmission khi browser refresh
        return "redirect:/account";
    }

    /**
     * Format lương sang định dạng hiển thị tiền tệ Việt Nam.
     * 
     * Ghi chú hiệu suất: NumberFormat.getCurrencyInstance() tương đối expensive.
     * Cân nhắc extract thành static final field hoặc inject CurrencyFormatter
     * service
     * nếu method này được gọi thường xuyên.
     * 
     * Defensive programming: Xử lý null inputs một cách graceful để ngăn NPE.
     * 
     * @param accountResponse response object để modify in-place (mutation by
     *                        design)
     *                        Ví dụ: 15000000 -> "15,000,000 ₫"
     */
    private void formatSalaryToVND(AccountResponse accountResponse) {
        // Early return pattern - giảm nesting và cải thiện readability
        if (accountResponse == null || accountResponse.getSalary() == null) {
            return; // No-op cho null inputs - ngăn unnecessary processing
        }

        // Tạo NumberFormat instance mỗi lần - acceptable cho low-frequency calls
        // TODO: Cân nhắc cache formatter này như static final để cải thiện performance
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Mutate response object trực tiếp - acceptable pattern cho presentation layer
        // formatting
        // Thay thế: Return formatted string và để caller quyết định assignment
        accountResponse.setFormattedSalary(vndFormat.format(accountResponse.getSalary()));
    }
}
