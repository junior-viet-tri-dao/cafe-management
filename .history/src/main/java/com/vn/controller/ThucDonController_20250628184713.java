package com.vn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.vn.repository.ThucDonRepository;

@Controller
public class ThucDonController {

    @Autowired
    private ThucDonRepository thucDonRepository;
    
    
}
