package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.menu.MenuDTO;
import com.viettridao.cafe.dto.menu.MenuDetailDTO;
import com.viettridao.cafe.exception.ResourceNotFoundException;
import com.viettridao.cafe.model.DonViTinh;
import com.viettridao.cafe.model.ThanhPhanThucDon;
import com.viettridao.cafe.model.ThucDon;
import com.viettridao.cafe.repository.DonViRepository;
import com.viettridao.cafe.repository.ThanhPhanThucDonRepository;
import com.viettridao.cafe.repository.ThucDonRepository;
import com.viettridao.cafe.service.ThucDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ThucDonServiceImpl implements ThucDonService {
    private final ThucDonRepository thucDonRepository;
    private final DonViRepository donViRepository;

    @Override
    public List<MenuDTO> getAllMenus() {
        return thucDonRepository.getAllMenus();
    }

    @Override
    public ThucDon createMenu(MenuDetailDTO menuDetailDTO) {
        ThucDon thucDon = new ThucDon();
        thucDon.setTenMon(menuDetailDTO.getTenMon());
        thucDon.setGiaTienHienTai(menuDetailDTO.getGiaTien());

        List<ThanhPhanThucDon> list = new ArrayList<>();
        menuDetailDTO.getThanhPhanList().forEach(e -> {
            ThanhPhanThucDon tp = new ThanhPhanThucDon();
            tp.setTenThanhPhan(e.getTenThanhPhan());
            tp.setKhoiLuong(e.getKhoiLuong());

            DonViTinh dv = donViRepository.findById(e.getMaDonViTinh()).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn vị tính id=" + e.getMaDonViTinh()));
            tp.setDonViTinh(dv);
            tp.setThucDon(thucDon);

            list.add(tp);
        });
        thucDon.setListThanhPhanThucDon(list);

        return thucDonRepository.save(thucDon);
    }


}
