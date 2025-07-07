package com.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.model.DonNhap;

@Repository
public interface DonNhapRepository extends JpaRepository<DonNhap, Integer> {


    

    
}
