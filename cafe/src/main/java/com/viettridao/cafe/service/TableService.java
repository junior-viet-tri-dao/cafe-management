package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.table.TableRequest;
import com.viettridao.cafe.model.TableEntity;

import java.util.List;

public interface TableService {
    List<TableEntity> getAllTables();
    TableEntity create(TableRequest request);
    TableEntity getTableById(Integer id);
}
