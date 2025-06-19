package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.AddNhanVienRequest;
import com.viettridao.cafe.dto.request.UpdateNhanVienRequest;
import com.viettridao.cafe.dto.response.NhanVienResponse;
import com.viettridao.cafe.model.NhanVien;

import java.util.List;

public interface NhanVienService {
    void addNhanVien(AddNhanVienRequest request);

    List<NhanVienResponse> getListNhanVien();

    NhanVien getNhanVienById(Integer maNhanVien);

    // Thêm method update
    void updateNhanVien(UpdateNhanVienRequest request);

    // Thêm method khóa nhân viên
    void lockEmployee(Integer maNhanVien);
}
