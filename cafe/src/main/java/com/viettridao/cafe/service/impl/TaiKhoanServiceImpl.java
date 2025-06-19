package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.LoginRequest;
import com.viettridao.cafe.dto.response.LoginResponse;
import com.viettridao.cafe.model.TaiKhoan;
import com.viettridao.cafe.repository.NhanVienRepository;
import com.viettridao.cafe.repository.TaiKhoanRepository;
import com.viettridao.cafe.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final NhanVienRepository nhanVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;

    @Override
    public LoginResponse login(LoginRequest loginDTO) {
        return nhanVienRepository.findThongTinDangNhap(loginDTO.getUsername());
    }

    @Override
    public TaiKhoan createTaiKhoan(String tenDangNhap, String matKhau) {
        TaiKhoan taiKhoan = new TaiKhoan();
        int maxId = taiKhoanRepository.findMaxMaNhanVien();
        int newId = maxId + 1;
        taiKhoan.setId(newId);
        // ID will be auto-generated due to @GeneratedValue annotation
        taiKhoan.setTenDangNhap(tenDangNhap);
        taiKhoan.setMatKhau(matKhau);

        // Set default values for other required fields
        taiKhoan.setQuyenHan("USER"); // or whatever default role you want
        // taiKhoan.setAnh(null); // This can be null if the column allows it

        // Log để debug
        System.out.println("Creating TaiKhoan: " + tenDangNhap);

        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoan);

        System.out.println("Saved TaiKhoan with ID: " + savedTaiKhoan.getId());

        return savedTaiKhoan;
    }
}