package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.AddNhanVienRequest;
import com.viettridao.cafe.dto.response.NhanVienResponse;
import com.viettridao.cafe.model.ChucVu;
import com.viettridao.cafe.model.NhanVien;
import com.viettridao.cafe.model.TaiKhoan;
import com.viettridao.cafe.repository.ChucVuRepository;
import com.viettridao.cafe.repository.NhanVienRepository;
import com.viettridao.cafe.repository.TaiKhoanRepository;
import com.viettridao.cafe.service.NhanVienService;
import com.viettridao.cafe.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {

    private final NhanVienRepository nhanVienRepository;
    private final ChucVuRepository chucVuRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final TaiKhoanService taiKhoanService;

    @Override
    public List<NhanVienResponse> getListNhanVien() {
        return nhanVienRepository.getListNhanVien();
    }

    @Override
    @Transactional
    public void addNhanVien(AddNhanVienRequest request) {
        try {
            // 1. Validation
            if (request.getHoTen() == null || request.getHoTen().trim().isEmpty()) {
                throw new RuntimeException("Họ tên không được để trống!");
            }

            // 2. Kiểm tra chức vụ
            ChucVu chucVu = chucVuRepository.findById(request.getMaChucVu())
                    .orElseThrow(() -> new RuntimeException("Chức vụ không tồn tại!"));

            // 3. Tạo tài khoản (nếu có thông tin)
            TaiKhoan taiKhoan = null;
            if (request.getTenDangNhap() != null && !request.getTenDangNhap().trim().isEmpty() &&
                    request.getMatKhau() != null && !request.getMatKhau().trim().isEmpty()) {

                // Kiểm tra tên đăng nhập đã tồn tại
                if (taiKhoanRepository.existsByTenDangNhap(request.getTenDangNhap().trim())) {
                    throw new RuntimeException("Tên đăng nhập đã tồn tại!");
                }

                System.out.println("Creating account for: " + request.getTenDangNhap());
                taiKhoan = taiKhoanService.createTaiKhoan(
                        request.getTenDangNhap().trim(),
                        request.getMatKhau().trim());
                System.out.println("Account created with ID: " + taiKhoan.getId());
            }

            int maxId = nhanVienRepository.findMaxMaNhanVien();
            int newId = maxId + 1;
            // 4. Tạo nhân viên
            NhanVien nhanVien = new NhanVien();
            nhanVien.setId(newId); // Giả sử ID được tạo ngẫu nhiên, có thể thay đổi theo logic của
                                                      // bạn
            nhanVien.setHoTen(request.getHoTen().trim());
            nhanVien.setDiaChi(request.getDiaChi());
            nhanVien.setSoDienThoai(request.getSoDienThoai());
            nhanVien.setMaChucVu(chucVu);
            nhanVien.setIsDeleted(false);

            // Set tài khoản nếu có
            if (taiKhoan != null) {
                nhanVien.setMaTaiKhoan(taiKhoan);
            }

            // 5. Lưu nhân viên
            System.out.println("Saving employee: " + nhanVien.getHoTen());
            NhanVien savedNhanVien = nhanVienRepository.save(nhanVien);
            System.out.println("Employee saved with ID: " + savedNhanVien.getId());

        } catch (Exception e) {
            System.err.println("Error adding employee: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm nhân viên: " + e.getMessage());
        }
    }
}
