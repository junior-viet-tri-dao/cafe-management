package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.AddNhanVienRequest;
import com.viettridao.cafe.dto.request.UpdateNhanVienRequest;
import com.viettridao.cafe.dto.response.NhanVienResponse;
import com.viettridao.cafe.model.NhanVien;

import java.util.List;

public interface NhanVienService {
    void addNhanVien(AddNhanVienRequest request);

    List<NhanVienResponse> getListNhanVien();

    // Add search method
    List<NhanVienResponse> searchNhanVienByName(String keyword);

    NhanVien getNhanVienById(Integer maNhanVien);

    void updateNhanVien(UpdateNhanVienRequest request);

    void lockEmployee(Integer maNhanVien);
}
