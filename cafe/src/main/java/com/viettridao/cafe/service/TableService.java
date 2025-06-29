package com.viettridao.cafe.service;

import com.viettridao.cafe.model.TableEntity;
import java.util.List;

public interface TableService {
    List<TableEntity> getAllTables();

    TableEntity getTableById(Integer id);

    TableEntity createTable(TableEntity table);

    TableEntity updateTable(Integer id, TableEntity table);

    void deleteTable(Integer id);

    TableEntity changeStatus(Integer id, String status);
}
