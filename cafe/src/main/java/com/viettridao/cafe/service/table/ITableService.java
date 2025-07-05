package com.viettridao.cafe.service.table;

import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.model.TableEntity;

import java.util.List;
import java.util.Map;

public interface ITableService {

    List<TableResponse> getAllTables();

    TableEntity getTableById(Integer tableId);

    TableResponse switchTable(Integer fromTable, Integer toTable);

    void mergeTables(List<Integer> mergeTableIds, Integer targetTableId);

    void splitTable(Integer sourceTableId, Integer targetTableId, Map<Integer, Integer> menuItemIdToQuantity);

}
