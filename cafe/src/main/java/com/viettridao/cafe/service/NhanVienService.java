package com.viettridao.cafe.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.NhanVien;
import com.viettridao.cafe.repository.NhanVienRepository;

@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository repo;

    public NhanVien login(String user, String pass) {
        return repo.findByUserAndPass(user, pass)
                   .filter(nv -> nv.getDaXoa() == null || !nv.getDaXoa())
                   .orElse(null);
    }

    public List<NhanVien> findAll() {
        return repo.findAllActive();
    }

    public NhanVien findById(String user) {
        return repo.findById(user)
                   .filter(nv -> nv.getDaXoa() == null || !nv.getDaXoa())
                   .orElse(null);
    }

    public void save(NhanVien nv) {
        if (nv.getDaXoa() == null) {
            nv.setDaXoa(false);
        }
        repo.save(nv);
    }

    public String xoaNhanVien(String username) {
        Optional<NhanVien> optional = repo.findById(username);
        if (optional.isEmpty()) {
            return "Không tìm thấy nhân viên.";
        }

        NhanVien nv = optional.get();

        // ❌ Không cho xóa nếu là ADMIN
        if (nv.getVaiTro() != null && nv.getVaiTro().name().equals("ADMIN")) {
            return "Không thể xóa tài khoản có vai trò ADMIN.";
        }

        nv.setDaXoa(true); // Xóa mềm
        repo.save(nv);
        return "Xóa nhân viên thành công.";
    }

    public void delete(String user) {
        Optional<NhanVien> optional = repo.findById(user);
        optional.ifPresent(nv -> {
            nv.setDaXoa(true);
            repo.save(nv);
        });
    }

    public boolean existsByUser(String user) {
        return repo.existsById(user);
    }

    public List<NhanVien> search(String keyword) {
        if (keyword == null || keyword.isBlank())
            return findAll();
        return repo.findByKeyword(keyword);
    }
}
