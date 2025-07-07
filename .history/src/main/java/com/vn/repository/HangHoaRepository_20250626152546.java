package com.vn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.model.HangHoa;
import com.vn.model.Users;

@Repository
public interface HangHoaRepository extends JpaRepository<HangHoa, Integer> {

    boolean existsByMaHangHoa(Integer maHangHoa);

    boolean existsByTenHangHoa(String tenHangHoa);
    
    @Query("SELECT u FROM Users u WHERE (:keyword IS NULL OR u.hoTen LIKE %:keyword%)")
    Page<Users> searchHangHoa(@Param("keyword") String keyword, Pageable pageable);
}
