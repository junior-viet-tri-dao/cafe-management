package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.commodity.UnitDTO;
import com.viettridao.cafe.model.DonViTinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonViRepository extends JpaRepository<DonViTinh, Integer> {
    @Query("""
    SELECT new com.viettridao.cafe.dto.commodity.UnitDTO(
        d.maDonViTinh,
        d.tenDonVi
    )
    FROM DonViTinh d
    """)
    List<UnitDTO> getAllUnits();
}
