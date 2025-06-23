package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.device.DeviceDTO;
import com.viettridao.cafe.dto.device.UpdateDeviceDTO;
import com.viettridao.cafe.exception.ResourceNotFoundException;
import com.viettridao.cafe.model.ThietBi;
import com.viettridao.cafe.repository.ThietBiRepository;
import com.viettridao.cafe.service.ThietBiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ThietBiServiceImpl implements ThietBiService {
    private final ThietBiRepository thietBiRepository;

    @Override
    public List<ThietBi> getAllThietBi() {
        return thietBiRepository.getAllDevice();
    }

    @Transactional
    @Override
    public ThietBi createDevice(DeviceDTO deviceDTO) {
        ThietBi tb = new ThietBi();
        tb.setTenThietBi(deviceDTO.getTenThietBi());
        tb.setNgayMua(deviceDTO.getNgayMua());
        tb.setDonGiaMua(deviceDTO.getDonGiaMua());
        tb.setSoLuong(deviceDTO.getSoLuong());
        tb.setIsDeleted(false);
        return thietBiRepository.save(tb);
    }

    @Transactional
    @Override
    public void deleteDevice(Integer id) {
        ThietBi tb = getDeviceById(id);
        tb.setIsDeleted(true);
        thietBiRepository.save(tb);
    }

    @Transactional
    @Override
    public void updateDevice(UpdateDeviceDTO deviceDTO) {
        ThietBi tb = getDeviceById(deviceDTO.getMaThietBi());
        tb.setTenThietBi(deviceDTO.getTenThietBi());
        tb.setNgayMua(deviceDTO.getNgayMua());
        tb.setDonGiaMua(deviceDTO.getDonGiaMua());
        tb.setSoLuong(deviceDTO.getSoLuong());

        thietBiRepository.save(tb);
    }

    @Override
    public ThietBi getDeviceById(Integer id) {
        return thietBiRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Thiết bị với id=" + id + " không tồn tại trong hệ thống"));
    }
}
