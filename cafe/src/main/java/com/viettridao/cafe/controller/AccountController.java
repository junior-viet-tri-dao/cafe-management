package com.viettridao.cafe.controller;


import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;


    @GetMapping("")
    public String account( Model model) {
        return "accounts/account";
    }

    @PostMapping("/update")
    public String accountUpdate(@ModelAttribute("account") UpdateAccountRequest request, RedirectAttributes redirectAttributes) {
        try {
            accountService.updateAccount(request);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/account";
    }

//    @GetMapping("/update/{id}")
//    public String accountUpdate(@ModelAttribute UpdateEmployeeRequest request, RedirectAttributes redirectAttributes) {
//        return "accounts/account";
//    }
}
