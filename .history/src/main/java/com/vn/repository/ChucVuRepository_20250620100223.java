package com.vn.repository;

import com.vn.model.ChucVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChucVuRepository extends JpaRepository<ChucVu, Integer> {
    ChucVu findByMaChucVu(Integer maChucVu);
    List<ChucVu> findAll(); 
}
