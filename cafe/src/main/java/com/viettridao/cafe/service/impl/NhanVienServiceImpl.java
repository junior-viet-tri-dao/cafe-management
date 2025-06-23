package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.employee.CreateEmployeeDTO;
import com.viettridao.cafe.dto.employee.EmployeeDTO;
import com.viettridao.cafe.dto.employee.UpdateEmployeeDTO;
import com.viettridao.cafe.exception.ResourceNotFoundException;
import com.viettridao.cafe.model.ChucVu;
import com.viettridao.cafe.model.NhanVien;
import com.viettridao.cafe.model.TaiKhoan;
import com.viettridao.cafe.repository.ChucVuRepository;
import com.viettridao.cafe.repository.NhanVienRepository;
import com.viettridao.cafe.repository.TaiKhoanRepository;
import com.viettridao.cafe.service.ChucVuService;
import com.viettridao.cafe.service.NhanVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NhanVienServiceImpl implements NhanVienService {
    private final NhanVienRepository nhanVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final ChucVuService chucVuService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public NhanVien getNhanVienById(int id) {
        return nhanVienRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên có id=" + id));
    }

    @Override
    public List<EmployeeDTO> getAllNhanVien() {
        return nhanVienRepository.getAllByEmployee();
    }

    @Transactional
    @Override
    public NhanVien createEmployee(CreateEmployeeDTO employeeDTO) {
        NhanVien nv = new NhanVien();
        nv.setSoDienThoai(employeeDTO.getSdt());
        nv.setDiaChi(employeeDTO.getDiaChi());
        nv.setHoTen(employeeDTO.getHoTen());
        nv.setIsDeleted(false);

        ChucVu cv = chucVuService.getChucVuById(Integer.parseInt(employeeDTO.getMaChucVu()));
        nv.setChucVu(cv);

        if (StringUtils.hasText(employeeDTO.getTenDangNhap()) && StringUtils.hasText(employeeDTO.getMatKhau())) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenDangNhap(employeeDTO.getTenDangNhap());
            taiKhoan.setMatKhau(passwordEncoder.encode(employeeDTO.getMatKhau()));
            taiKhoan.setQuyenHan("EMPLOYEE");

            taiKhoan.setNhanVien(nv);
            nv.setTaiKhoan(taiKhoan);
        }

        return nhanVienRepository.save(nv);
    }

    @Override
    public void deleteEmployee(Integer id) {
        NhanVien nv = getNhanVienById(id);
        nv.setIsDeleted(true);

        nhanVienRepository.save(nv);
    }

    @Override
    public UpdateEmployeeDTO getEmployeeById(Integer id) {
        if(id !=null){
            return nhanVienRepository.getEmployeeById(id);
        }
        return null;
    }

    @Transactional
    @Override
    public void updateEmployee(UpdateEmployeeDTO employeeDTO) {
        NhanVien nv = getNhanVienById(employeeDTO.getMaNhanVien());
        nv.setSoDienThoai(employeeDTO.getSdt());
        nv.setDiaChi(employeeDTO.getDiaChi());
        nv.setHoTen(employeeDTO.getHoTen());

        ChucVu cv = chucVuService.getChucVuById(employeeDTO.getMaChucVu());
        nv.setChucVu(cv);

        if (StringUtils.hasText(employeeDTO.getTenDangNhap()) && StringUtils.hasText(employeeDTO.getMatKhau())) {
            Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByUsername(employeeDTO.getTenDangNhap());

            TaiKhoan taiKhoan;
            if (taiKhoanOpt.isPresent()) {
                taiKhoan = taiKhoanOpt.get();
                taiKhoan.setMatKhau(passwordEncoder.encode(employeeDTO.getMatKhau()));
            } else {
                taiKhoan = new TaiKhoan();
                taiKhoan.setTenDangNhap(employeeDTO.getTenDangNhap());
                taiKhoan.setMatKhau(passwordEncoder.encode(employeeDTO.getMatKhau()));
                taiKhoan.setQuyenHan("EMPLOYEE");
                taiKhoan.setNhanVien(nv);
                nv.setTaiKhoan(taiKhoan);
            }
            taiKhoanRepository.save(taiKhoan);
        }
        nhanVienRepository.save(nv);
    }

}
