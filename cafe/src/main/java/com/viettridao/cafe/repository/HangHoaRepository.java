package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.commodity.CommodityAllDTO;
import com.viettridao.cafe.dto.commodity.CommodityDTO;
import com.viettridao.cafe.dto.commodity.UpdateCommodityDTO;
import com.viettridao.cafe.model.HangHoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HangHoaRepository extends JpaRepository<HangHoa, Integer> {
    @Query("""
    SELECT new com.viettridao.cafe.dto.commodity.CommodityDTO(
        n.maDonNhap, h.tenHangHoa, n.ngayNhap, null, n.soLuong, null, t.tenDonVi, h.donGia)
    FROM DonNhap n
    JOIN n.hangHoa h
    JOIN h.donVi t
    """)
    List<CommodityDTO> getAllNhap();

    @Query("""
    SELECT new com.viettridao.cafe.dto.commodity.CommodityDTO(
        null ,h.tenHangHoa, null, x.ngayXuat, null, x.soLuong, t.tenDonVi, h.donGia)
    FROM DonXuat x
    JOIN x.hangHoa h
    JOIN h.donVi t
    """)
    List<CommodityDTO> getAllXuat();


    @Query("""
    SELECT new com.viettridao.cafe.dto.commodity.CommodityAllDTO(
        h.maHangHoa,
        h.tenHangHoa, 
        dv.tenDonVi
    )
    FROM HangHoa h join h.donVi dv
    """)
    List<CommodityAllDTO> getAllCommodity();

    @Query("""
    SELECT new com.viettridao.cafe.dto.commodity.UpdateCommodityDTO(
        n.maDonNhap,
        h.maHangHoa,
        n.ngayNhap,
        n.soLuong,
        h.donGia,
        dv.tenDonVi
    )
    FROM DonNhap n join n.hangHoa h join h.donVi dv where n.maDonNhap = :id
    """)
    UpdateCommodityDTO getUpdateCommodityDTO(Integer id);
}
