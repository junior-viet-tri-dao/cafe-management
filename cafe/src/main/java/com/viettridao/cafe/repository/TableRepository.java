package com.viettridao.cafe.repository;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {

    List<TableEntity> findAllByDeletedFalse();

    List<TableEntity> findByStatus(TableStatus status);
}
