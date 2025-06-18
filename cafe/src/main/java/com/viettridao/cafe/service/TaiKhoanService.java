package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.LoginRequest;
import com.viettridao.cafe.dto.response.ThongTinDangNhapResponse;

public interface TaiKhoanService {
    ThongTinDangNhapResponse login(LoginRequest loginDTO);
}
