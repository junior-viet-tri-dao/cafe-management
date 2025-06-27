package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ImportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {
}
