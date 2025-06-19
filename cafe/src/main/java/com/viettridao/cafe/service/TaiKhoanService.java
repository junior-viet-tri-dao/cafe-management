package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.LoginRequest;
import com.viettridao.cafe.dto.response.LoginResponse;

public interface TaiKhoanService {
    LoginResponse login(LoginRequest loginDTO);
}
