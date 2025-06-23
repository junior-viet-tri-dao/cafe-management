package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.device.DeviceDTO;
import com.viettridao.cafe.dto.device.UpdateDeviceDTO;
import com.viettridao.cafe.model.ThietBi;

import java.util.List;

public interface ThietBiService {
    List<ThietBi> getAllThietBi();
    ThietBi createDevice(DeviceDTO deviceDTO);
    void deleteDevice(Integer id);
    void updateDevice(UpdateDeviceDTO deviceDTO);
    ThietBi getDeviceById(Integer id);
}
