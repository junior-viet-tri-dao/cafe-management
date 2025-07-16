package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.mapper.AccountMapper;
import com.viettridao.cafe.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountResponse accountResponse = accountMapper.toAccountResponse(accountService.getAccountByUsername(auth.getName()));
        model.addAttribute("account", accountResponse != null ? accountResponse : new AccountResponse());
        return "/accounts/account";
    }

    @PostMapping("/update")
    public String updateAccount(@Valid @ModelAttribute("account") UpdateAccountRequest request, BindingResult result,
                                RedirectAttributes redirectAttributes) {
        try {
            if(result.hasErrors()){
                return "/accounts/account";
            }
            accountService.updateAccount(request);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật thất bại: " + e.getMessage());
        }
        return "redirect:/account";
    }

}
