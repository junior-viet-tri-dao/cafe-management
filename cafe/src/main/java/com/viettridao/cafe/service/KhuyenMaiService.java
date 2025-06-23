package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.marketing.CreateMarketingDTO;
import com.viettridao.cafe.dto.marketing.MarketingDTO;
import com.viettridao.cafe.model.KhuyenMai;

import java.util.List;

public interface KhuyenMaiService {
    List<MarketingDTO> getAllMarketings();
    KhuyenMai createMarketing(CreateMarketingDTO marketing);
    void deleteMarketing(Integer id);
    void updateMarketing(MarketingDTO marketing);
    KhuyenMai getMarketingById(Integer id);
    MarketingDTO getMarketingDTOById(Integer id);
}
