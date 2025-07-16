package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.table.CreateTableRequest;
import com.viettridao.cafe.dto.request.table.SplitItemRequest;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.model.UnitEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TableService {
    List<TableEntity> getAllTables();

    TableEntity createTable(CreateTableRequest request);

    TableEntity getTableById( Integer tableId);

    void cancelTable(Integer tableId);

    void selectMenusForTable(Integer tableId, List<Integer> menuIds, List<Integer> quantities);

    void payment(Integer tableId);

    void moveTable(Integer fromTableId, Integer toTableId);

    void merge(List<Integer> sourceTableIds, Integer targetTableId);

    void splitTable(Integer fromTableId, Integer toTableId, List<SplitItemRequest> items);
}
