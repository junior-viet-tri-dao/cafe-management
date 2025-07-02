package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.tables.MenuItemSplitRequest;

public interface TableSplitService {
    void splitTable(Integer fromTableId, Integer toTableId, List<MenuItemSplitRequest> itemsToSplit, String customerName, String customerPhone);

}

