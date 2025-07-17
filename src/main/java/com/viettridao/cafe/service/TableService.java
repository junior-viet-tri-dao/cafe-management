package com.viettridao.cafe.service;

import java.util.List;
import java.util.Map;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.table.SwitchTableRequest;
import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.model.TableEntity;

public interface TableService {
    List<TableResponse> getAllTables();

    TableResponse switchTable(SwitchTableRequest request);

    List<TableResponse> getTableByStatus(TableStatus status);

    TableEntity getTableById(Integer tableId);

    void mergeTables(List<Integer> mergeTableIds, Integer targetTableId);

    void splitTable(Integer sourceTableId, Integer targetTableId, Map<Integer, Integer> menuItemIdToQuantity);

}
