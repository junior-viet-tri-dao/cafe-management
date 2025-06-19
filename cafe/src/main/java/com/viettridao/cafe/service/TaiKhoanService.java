package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.LoginRequest;
import com.viettridao.cafe.dto.response.LoginResponse;
import com.viettridao.cafe.model.TaiKhoan;

public interface TaiKhoanService {
    LoginResponse login(LoginRequest loginDTO);

    TaiKhoan createTaiKhoan(String tenDangNhap, String matKhau);

    TaiKhoan findTaiKhoanById(Integer id);
}
