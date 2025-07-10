package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.response.warehouse_transaction.WarehouseTransactionPageResponse;
import com.viettridao.cafe.dto.response.warehouse_transaction.WarehouseTransactionResponse;

public interface WarehouseTransactionService {
    WarehouseTransactionPageResponse getTransactions(String keyword, int page, int size);
}