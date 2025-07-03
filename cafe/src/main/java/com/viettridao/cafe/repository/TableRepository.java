package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {
    Optional<TableEntity> findByTableName(String name);
}
