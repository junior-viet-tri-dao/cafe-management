package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.mapper.AccountMapper;
import com.viettridao.cafe.service.AccountService;
import lombok.RequiredArgsConstructor;
import com.viettridao.cafe.model.AccountEntity;

import org.springframework.security.core.userdetails.User;
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
    private final AccountMapper accountMapper;

    @GetMapping("")
    public String home(@ModelAttribute("user") User user, Model model) {
        AccountResponse accountResponse = accountMapper
                .toAccountResponse(accountService.getAccountByUsername(user.getUsername()));

        model.addAttribute("account", accountResponse);
        return "/accounts/account";
    }

    @PostMapping("/update")
    public String updateAccount(UpdateAccountRequest request,
            RedirectAttributes redirectAttributes) {
        try {
            accountService.updateAccount(request);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/account";
    }

}
