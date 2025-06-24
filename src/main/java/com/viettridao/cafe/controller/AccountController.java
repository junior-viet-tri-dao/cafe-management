package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AccountController extends BaseController {
    @GetMapping("/account")
    public String showAccount() {
        return "accounts/account";
    }
}
