package com.viettridao.cafe.controller;

import jakarta.servlet.http.HttpSession;

public abstract class BaseController {

    protected boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("isLoggedIn") != null && (Boolean) session.getAttribute("isLoggedIn");
    }

    protected String redirectToLoginIfNotAuthenticated(HttpSession session) {
        return isAuthenticated(session) ? null : "redirect:/";
    }
}
