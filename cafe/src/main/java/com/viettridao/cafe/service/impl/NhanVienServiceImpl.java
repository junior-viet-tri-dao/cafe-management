package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.response.NhanVienResponse;
import com.viettridao.cafe.repository.NhanVienRepository;
import com.viettridao.cafe.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {

    private final NhanVienRepository nhanVienRepository;

    @Override
    public List<NhanVienResponse> getListNhanVien() {
        return nhanVienRepository.getListNhanVien();
    }

}
