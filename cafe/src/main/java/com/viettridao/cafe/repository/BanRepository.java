package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.Ban;

@Repository
public interface BanRepository extends JpaRepository<Ban, Long> {
	List<Ban> findAllByDaXoaFalse();
}
