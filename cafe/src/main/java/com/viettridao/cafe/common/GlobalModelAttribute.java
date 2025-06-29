package com.viettridao.cafe.common;

import com.viettridao.cafe.model.AccountEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttribute {

    @ModelAttribute("currentPath")
    public String populateCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("user")
    public Object addUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return authentication.getPrincipal();
        }
        return null;
    }
}
