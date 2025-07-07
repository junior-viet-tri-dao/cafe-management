package com.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.model.HangHoa;

@Repository
public interface HangHoaRepository extends JpaRepository<HangHoa, Integer> {

    boolean existsByMaHangHoa(String maHangHoa);

    boolean existsByTenHangHoa(String tenHangHoa);

}
