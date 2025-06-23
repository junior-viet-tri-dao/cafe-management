package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.commodity.*;
import com.viettridao.cafe.exception.ResourceNotFoundException;
import com.viettridao.cafe.model.DonNhap;
import com.viettridao.cafe.model.DonViTinh;
import com.viettridao.cafe.model.DonXuat;
import com.viettridao.cafe.model.HangHoa;
import com.viettridao.cafe.repository.DonNhapRepository;
import com.viettridao.cafe.repository.DonViRepository;
import com.viettridao.cafe.repository.DonXuatRepository;
import com.viettridao.cafe.repository.HangHoaRepository;
import com.viettridao.cafe.service.HangHoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HangHoaServiceImpl implements HangHoaService {
    private final HangHoaRepository hangHoaRepository;
    private final DonXuatRepository donXuatRepository;
    private final DonViRepository DonViRepository;
    private final DonViRepository donViRepository;
    private final DonNhapRepository donNhapRepository;

    @Override
    public List<CommodityDTO> getAllCommodities() {
        List<CommodityDTO> commodities = new ArrayList<>();
        commodities.addAll(hangHoaRepository.getAllNhap());
        commodities.addAll(hangHoaRepository.getAllXuat());
        return commodities;
    }

    @Override
    public List<CommodityAllDTO> getAllCommodity() {
        return hangHoaRepository.getAllCommodity();
    }

    @Transactional
    @Override
    public DonNhap createCommodity(CreateCommodityDTO createCommodityDTO) {
        HangHoa hangHoa = getCommodityById(createCommodityDTO.getMaHangHoa());

        DonNhap donNhap = new DonNhap();
        donNhap.setHangHoa(hangHoa);
        donNhap.setSoLuong(createCommodityDTO.getSoLuongNhap());
        donNhap.setNgayNhap(createCommodityDTO.getNgayNhap());
        donNhap.setTongTien(createCommodityDTO.getDonGia() * createCommodityDTO.getSoLuongNhap());

        hangHoa.setSoLuong(hangHoa.getSoLuong() + donNhap.getSoLuong());
        hangHoaRepository.save(hangHoa);

        return donNhapRepository.save(donNhap);
    }

    @Override
    public HangHoa getCommodityById(Integer id) {
        return hangHoaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy hàng hóa có id=" + id));
    }

    @Override
    public DonViTinh getUnitById(Integer id) {
        return donViRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy đơn vị tính có id=" + id));
    }

    @Transactional
    @Override
    public DonXuat createCommodityExport(CreateCommodityExportDTO exportDTO) {
        HangHoa hangHoa = getCommodityById(exportDTO.getMaHangHoa());

        DonXuat xuat = new DonXuat();
        xuat.setHangHoa(hangHoa);
        xuat.setSoLuong(exportDTO.getSoLuong());
        xuat.setNgayXuat(exportDTO.getNgayXuat());
        xuat.setTongTienXuat(hangHoa.getDonGia() * exportDTO.getSoLuong());

        hangHoa.setSoLuong(hangHoa.getSoLuong() - exportDTO.getSoLuong());

        hangHoaRepository.save(hangHoa);
        return donXuatRepository.save(xuat);
    }

    @Override
    public UpdateCommodityDTO getUpdateCommodityDTO(Integer id) {
        if(id != null){
            return hangHoaRepository.getUpdateCommodityDTO(id);
        }
        return null;
    }

    @Transactional
    @Override
    public void updateCommodity(UpdateCommodityDTO dto) {
        DonNhap donNhap = donNhapRepository.findById(dto.getMaDonNhap())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn nhập với id = " + dto.getMaDonNhap()));

        HangHoa oldHangHoa = donNhap.getHangHoa();
        int oldSoLuong = donNhap.getSoLuong();

        HangHoa newHangHoa = getCommodityById(dto.getMaHangHoa());

        donNhap.setNgayNhap(dto.getNgayNhap());
        donNhap.setSoLuong(dto.getSoLuongNhap());

        if (oldHangHoa.getMaHangHoa().equals(newHangHoa.getMaHangHoa())) {
            int delta = dto.getSoLuongNhap() - oldSoLuong;
            oldHangHoa.setSoLuong(oldHangHoa.getSoLuong() + delta);

            donNhap.setTongTien(dto.getSoLuongNhap() * oldHangHoa.getDonGia());
        } else {
            oldHangHoa.setSoLuong(oldHangHoa.getSoLuong() - oldSoLuong);

            newHangHoa.setSoLuong(newHangHoa.getSoLuong() + dto.getSoLuongNhap());

            donNhap.setHangHoa(newHangHoa);

            donNhap.setTongTien(dto.getSoLuongNhap() * newHangHoa.getDonGia());
        }

        donNhapRepository.save(donNhap);
    }


}
