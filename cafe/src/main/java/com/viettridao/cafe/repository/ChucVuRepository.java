package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.position.PositionDTO;
import com.viettridao.cafe.model.ChucVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChucVuRepository extends JpaRepository<ChucVu, Integer> {
    @Query("""
    select new com.viettridao.cafe.dto.position.PositionDTO(cv.maChucVu, cv.tenChucVu, cv.luong)
    from ChucVu cv
    """)
    List<PositionDTO> getAllByChucVu();
}
