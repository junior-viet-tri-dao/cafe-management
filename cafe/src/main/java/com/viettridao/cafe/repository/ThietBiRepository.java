package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThietBiRepository extends JpaRepository<ThietBi, Integer> {
    @Query("select t from ThietBi t where t.isDeleted = false ")
    List<ThietBi> getAllDevice();
}
