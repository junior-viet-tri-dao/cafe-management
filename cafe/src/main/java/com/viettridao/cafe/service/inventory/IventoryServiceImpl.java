package com.viettridao.cafe.service.inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.response.inventory.InventoryTransactionResponse;
import com.viettridao.cafe.mapper.InventoryTransactionMapper;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;

@Service
@RequiredArgsConstructor
public class IventoryServiceImpl implements IInventoryService {

    private final InventoryTransactionMapper inventoryTransactionMapper;
    private final ImportRepository importRepository;
    private final ExportRepository exportRepository;

    @Override
    public List<InventoryTransactionResponse> getAllTransactions() {
        List<InventoryTransactionResponse> result = new ArrayList<>();

        importRepository.findAllByDeletedFalse()
                .forEach(imp -> result.add(inventoryTransactionMapper.fromImport(imp)));

        exportRepository.findAllByDeletedFalse()
                .forEach(exp -> result.add(inventoryTransactionMapper.fromExport(exp)));

        result.sort(Comparator.comparing(
                tx -> tx.getImportDate() != null ? tx.getImportDate() : tx.getExportDate(),
                Comparator.reverseOrder())
        );

        return result;
    }
}
