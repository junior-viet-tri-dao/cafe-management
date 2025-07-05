package com.viettridao.cafe.common;

import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.service.account.IAccountService;

import com.viettridao.cafe.service.menuItem.IMenuItemService;
import com.viettridao.cafe.service.table.ITableService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {

    private final IAccountService accountService;
    private final ITableService tableService;
    private final IMenuItemService menuItemService;

    @ModelAttribute("currentPath")
    public String populateCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("user")
    public AccountEntity addUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            return (AccountEntity) auth.getPrincipal();
        }
        return null;
    }

    @ModelAttribute("user")
    public AccountEntity currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof AccountEntity) {
            return (AccountEntity) auth.getPrincipal();
        }
        return null;
    }

    @ModelAttribute("currentUser")
    public EmployeeEntity addCurrentUserToModel(HttpSession session) {
        // Try to get from session
        EmployeeEntity currentUser = (EmployeeEntity) session.getAttribute("currentUser");
        if (currentUser != null) {
            return currentUser;
        }

        // Fallback to Authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            AccountEntity account = (AccountEntity) auth.getPrincipal();
            if (account != null && account.getEmployee() != null) {
                currentUser = account.getEmployee();
                session.setAttribute("currentUser", currentUser); // Set in session
                return currentUser;
            }
        }
        return null; // Return null instead of throwing to avoid breaking non-authenticated flows
    }

}
