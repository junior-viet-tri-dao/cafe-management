package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.commodity.*;
import com.viettridao.cafe.model.DonNhap;
import com.viettridao.cafe.model.DonViTinh;
import com.viettridao.cafe.model.DonXuat;
import com.viettridao.cafe.model.HangHoa;

import java.util.List;

public interface HangHoaService {
    List<CommodityDTO> getAllCommodities();
    List<CommodityAllDTO> getAllCommodity();
    DonNhap createCommodity(CreateCommodityDTO createCommodityDTO);
    HangHoa getCommodityById(Integer id);
    DonViTinh getUnitById(Integer id);
    DonXuat createCommodityExport(CreateCommodityExportDTO exportDTO);
    UpdateCommodityDTO getUpdateCommodityDTO(Integer id);
    void updateCommodity(UpdateCommodityDTO updateCommodityDTO);
}
