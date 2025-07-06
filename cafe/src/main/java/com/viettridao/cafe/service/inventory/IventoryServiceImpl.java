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

        // Lấy danh sách import và map
        importRepository.findAllByDeletedFalse()
                .forEach(imp -> {
                    try {
                        result.add(inventoryTransactionMapper.fromImport(imp));
                    } catch (Exception e) {
                        System.err.println("Lỗi khi mapping import: " + e.getMessage());
                    }
                });

        // Lấy danh sách export và map
        exportRepository.findAllByDeletedFalse()
                .forEach(exp -> {
                    try {
                        result.add(inventoryTransactionMapper.fromExport(exp));
                    } catch (Exception e) {
                        System.err.println("Lỗi khi mapping export: " + e.getMessage());
                    }
                });

        // Sắp xếp theo ngày gần nhất
        result.sort(Comparator.comparing(
                tx -> tx.getImportDate() != null ? tx.getImportDate() : tx.getExportDate(),
                Comparator.reverseOrder())
        );

        return result;
    }
}
