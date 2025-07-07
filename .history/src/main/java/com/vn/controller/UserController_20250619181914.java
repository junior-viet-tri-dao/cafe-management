package com.vn.controller;

import com.vn.model.Users;
import com.vn.model.ChucVu;
import com.vn.repository.UserRepository;
import com.vn.repository.ChucVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChucVuRepository chucVuRepository;

    @GetMapping("/trangcanhan")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        ChucVu chucVu = null;
        if (user.getChucVu() != null && user.getChucVu().getMaChucVu() != null) {
            chucVu = chucVuRepository.findByMaChucVu(user.getChucVu().getMaChucVu());
        }
        model.addAttribute("user", user);
        model.addAttribute("chucVu", chucVu);
        return "trangcanhan";
    }

    @GetMapping("/admin/employee/list")
    public String listEmployees(Model model) {
        List<Users> employees = userRepository.findAll();
        model.addAttribute("employees", employees);
        return "admin/employee/list";
    }
}
