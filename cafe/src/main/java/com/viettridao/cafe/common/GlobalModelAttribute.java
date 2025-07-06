package com.viettridao.cafe.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttribute {

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken)) {
            AccountEntity account = (AccountEntity) auth.getPrincipal();
            if (account != null && account.getEmployee() != null) {
                currentUser = account.getEmployee();
                session.setAttribute("currentUser", currentUser);
                return currentUser;
            }
        }
        return null;
    }

}
