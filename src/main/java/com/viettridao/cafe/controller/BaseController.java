package com.viettridao.cafe.controller;

import com.viettridao.cafe.model.AccountEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

public abstract class BaseController {

    @ModelAttribute("user")
    public AccountEntity getCurrentUser(HttpSession session) {
        return (AccountEntity) session.getAttribute("user");
    }

    @ModelAttribute("isAuthenticated")
    public Boolean isAuthenticated(HttpSession session) {
        Boolean auth = (Boolean) session.getAttribute("isAuthenticated");
        return auth != null && auth;
    }

    @ModelAttribute("currentPath")
    public String getCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
