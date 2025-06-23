package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.account.AccountDTO;
import com.viettridao.cafe.exception.ResourceNotFoundException;
import com.viettridao.cafe.model.NhanVien;
import com.viettridao.cafe.model.TaiKhoan;
import com.viettridao.cafe.repository.ChucVuRepository;
import com.viettridao.cafe.repository.NhanVienRepository;
import com.viettridao.cafe.repository.TaiKhoanRepository;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.ChucVuService;
import com.viettridao.cafe.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhanVienRepository nhanVienRepository;
    private final ChucVuRepository chucVuRepository;

    @Override
    public AccountDTO getAccount(String username) {
        if(StringUtils.hasText(username)) {
            return taiKhoanRepository.findAccountByUsername(username);
        }else {
            throw new RuntimeException("Vui lòng nhập đầy đủ thông tin");
        }
    }

    @Override
    public void updateAccount(AccountDTO account) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByUsername(account.getTenDangNhap()).orElseThrow(()->
                new ResourceNotFoundException("Không tìm thấy tài khoản có username=" + account.getTenDangNhap()));

        if(taiKhoan.getNhanVien() != null) {
            NhanVien nv = taiKhoan.getNhanVien();
            nv.setDiaChi(account.getDiaChi());
            nv.setHoTen(account.getHoTen());
            nv.setSoDienThoai(account.getSdt());

            nhanVienRepository.save(nv);
        }
    }
}
