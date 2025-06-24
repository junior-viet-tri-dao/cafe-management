package com.viettridao.cafe.common;

import com.viettridao.cafe.model.AccountEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Lớp cung cấp các thuộc tính toàn cục cho các controller trong ứng dụng.
 * Sử dụng @ControllerAdvice để tự động thêm các thuộc tính vào model của mọi request.
 */
@ControllerAdvice
public class GlobalModelAttribute {

    /**
     * Thêm đường dẫn hiện tại của request vào model với tên "currentPath".
     * @param request Đối tượng HttpServletRequest chứa thông tin request
     * @return Chuỗi đường dẫn URI của request hiện tại
     */
    @ModelAttribute("currentPath")
    public String populateCurrentPath(HttpServletRequest request) {
        // Trả về URI của request (ví dụ: /home, /login)
        return request.getRequestURI();
    }

    /**
     * Thêm thông tin người dùng hiện tại vào model với tên "user".
     * @return AccountEntity nếu người dùng đã xác thực, null nếu chưa xác thực hoặc là người dùng ẩn danh
     */
    @ModelAttribute("user")
    public AccountEntity addUser() {
        // Lấy thông tin xác thực từ SecurityContextHolder
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Kiểm tra nếu xác thực tồn tại, đã được xác thực và không phải là người dùng ẩn danh
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            // Trả về AccountEntity từ principal của đối tượng xác thực
            return (AccountEntity) auth.getPrincipal();
        }
        
        // Trả về null nếu không có người dùng hợp lệ
        return null;
    }
}