package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.marketing.CreateMarketingDTO;
import com.viettridao.cafe.dto.marketing.MarketingDTO;
import com.viettridao.cafe.exception.ResourceNotFoundException;
import com.viettridao.cafe.model.KhuyenMai;
import com.viettridao.cafe.repository.KhuyenMaiRepository;
import com.viettridao.cafe.service.KhuyenMaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class KhuyenMaiServiceImpl implements KhuyenMaiService {
    private final KhuyenMaiRepository khuyenMaiRepository;

    @Override
    public List<MarketingDTO> getAllMarketings() {
        return khuyenMaiRepository.getAllMarketings();
    }

    @Transactional
    @Override
    public KhuyenMai createMarketing(CreateMarketingDTO marketing) {
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setTenKhuyenMai(marketing.getTenKhuyenMai());
        khuyenMai.setNgayBatDau(marketing.getNgayBatDau());
        khuyenMai.setNgayKetThuc(marketing.getNgayKetThuc());
        khuyenMai.setGiaTriGiam(marketing.getGiaTriGiam());
        khuyenMai.setIsDeleted(false);

        return khuyenMaiRepository.save(khuyenMai);
    }

    @Transactional
    @Override
    public void deleteMarketing(Integer id) {
        KhuyenMai khuyenMai = getMarketingById(id);
        khuyenMai.setIsDeleted(true);

        khuyenMaiRepository.save(khuyenMai);
    }

    @Transactional
    @Override
    public void updateMarketing(MarketingDTO marketing) {
        KhuyenMai khuyenMai = getMarketingById(marketing.getMaKhuyenMai());
        khuyenMai.setTenKhuyenMai(marketing.getTenKhuyenMai());
        khuyenMai.setNgayBatDau(marketing.getNgayBatDau());
        khuyenMai.setNgayKetThuc(marketing.getNgayKetThuc());
        khuyenMai.setGiaTriGiam(marketing.getGiaTriGiam());

        khuyenMaiRepository.save(khuyenMai);
    }

    @Override
    public KhuyenMai getMarketingById(Integer id) {
        return khuyenMaiRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy khuyến mãi có id=" + id));
    }

    @Override
    public MarketingDTO getMarketingDTOById(Integer id) {
        if(id != null){
            return khuyenMaiRepository.getMarketingById(id);
        }
        return null;
    }
}
