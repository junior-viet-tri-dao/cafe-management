package com.vn.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class BaseUrlInterceptor implements HandlerInterceptor {

    @Autowired
    private ServletContext servletContext;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String baseUrl = servletContext.getContextPath();

        baseUrl += "/";

        request.setAttribute("baseUrl", baseUrl);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
            modelAndView.addObject("baseUrl", request.getAttribute("baseUrl"));
        }
    }
}
