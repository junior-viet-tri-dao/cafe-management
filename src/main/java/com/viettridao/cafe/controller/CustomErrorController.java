package com.viettridao.cafe.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorCode", "404");
                model.addAttribute("errorTitle", "Trang không tìm thấy");
                model.addAttribute("errorMessage", "Trang bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.");
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorCode", "500");
                model.addAttribute("errorTitle", "Lỗi hệ thống");
                model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
                return "error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorCode", "403");
                model.addAttribute("errorTitle", "Truy cập bị từ chối");
                model.addAttribute("errorMessage", "Bạn không có quyền truy cập vào trang này.");
                return "error/403";
            }
        }

        // Default error page
        model.addAttribute("errorCode", "Lỗi");
        model.addAttribute("errorTitle", "Đã xảy ra lỗi");
        model.addAttribute("errorMessage", "Một lỗi không xác định đã xảy ra. Vui lòng thử lại sau.");
        return "error/error";
    }
}
