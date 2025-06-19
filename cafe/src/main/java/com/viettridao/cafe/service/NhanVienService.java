package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.AddNhanVienRequest;
import com.viettridao.cafe.dto.response.NhanVienResponse;

import java.util.List;

public interface NhanVienService {
    List<NhanVienResponse> getListNhanVien();

    void addNhanVien(AddNhanVienRequest request);
}
