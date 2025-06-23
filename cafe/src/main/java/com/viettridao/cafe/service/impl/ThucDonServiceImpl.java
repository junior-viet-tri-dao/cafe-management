package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.menu.MenuDTO;
import com.viettridao.cafe.repository.ThucDonRepository;
import com.viettridao.cafe.service.ThucDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThucDonServiceImpl implements ThucDonService {
    private final ThucDonRepository thucDonRepository;

    @Override
    public List<MenuDTO> getAllMenus() {
        return thucDonRepository.getAllMenus();
    }
}
