package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.LoginRequest;
import com.viettridao.cafe.dto.response.LoginResponse;
import com.viettridao.cafe.repository.NhanVienRepository;
import com.viettridao.cafe.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final NhanVienRepository nhanVienRepository;

    @Override
    public LoginResponse login(LoginRequest loginDTO) {
        return nhanVienRepository.findThongTinDangNhap(loginDTO.getUsername());
    }
}
