package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.account.AccountDTO;
import com.viettridao.cafe.model.TaiKhoan;

import java.util.List;

public interface TaiKhoanService {
    boolean login(String username, String password);
    List<TaiKhoan> getAllTaiKhoan();
}
