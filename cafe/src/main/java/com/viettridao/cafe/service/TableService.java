package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.table.TableRequest;
import com.viettridao.cafe.model.TableEntity;

import java.util.List;

public interface TableService {
    List<TableEntity> getAllTables();
    TableEntity create(TableRequest request);
    TableEntity getTableById(Integer id);
    void cancelTable(Integer id);
    void selectMenusForTable(Integer tableId, List<Integer> menuIds, List<Integer> quantities);
    void payment(Integer tableId);
    void moveTable(Integer fromTableId, Integer toTableId);
    void merge(List<Integer> sourceTableIds, Integer targetTableId);
}
