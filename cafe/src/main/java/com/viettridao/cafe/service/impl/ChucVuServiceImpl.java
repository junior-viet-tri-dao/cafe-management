package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.position.PositionDTO;
import com.viettridao.cafe.exception.ResourceNotFoundException;
import com.viettridao.cafe.model.ChucVu;
import com.viettridao.cafe.repository.ChucVuRepository;
import com.viettridao.cafe.service.ChucVuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChucVuServiceImpl implements ChucVuService {
    private final ChucVuRepository chucVuRepository;

    @Override
    public ChucVu getChucVuById(int id) {
        return chucVuRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chức vụ có id=" + id));
    }

    @Override
    public List<PositionDTO> getAllChucVu() {
        return chucVuRepository.getAllByChucVu();
    }
}
