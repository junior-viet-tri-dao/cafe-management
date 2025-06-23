package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.position.PositionDTO;
import com.viettridao.cafe.model.ChucVu;

import java.util.List;

public interface ChucVuService {
    ChucVu getChucVuById(int id);
    List<PositionDTO> getAllChucVu();
}
