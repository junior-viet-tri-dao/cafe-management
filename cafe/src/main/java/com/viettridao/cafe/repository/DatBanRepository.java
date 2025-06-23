package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.DatBan;

@Repository
public interface DatBanRepository extends JpaRepository<DatBan, Long> {
    Optional<DatBan> findByBan_IdAndDaXoaFalse(Long banId);
    List<DatBan> findAllByDaXoaFalse();
}
