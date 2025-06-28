package com.viettridao.cafe.service.inventory;

import java.util.List;

import com.viettridao.cafe.dto.response.inventory.InventoryTransactionResponse;

public interface IInventoryService {

    List<InventoryTransactionResponse> getAllTransactions();
}
