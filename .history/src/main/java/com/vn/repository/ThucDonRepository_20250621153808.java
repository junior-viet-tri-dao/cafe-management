package com.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.model.ThucDon;

@Repository
public  interface ThucDonRepository extends JpaRepository<ThucDon, Integer> {
    // Define custom query methods if needed
    
}
