package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;

    @Override
    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    @Override
    public TableEntity getTableById(Integer id) {
        return tableRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với id=" + id));
    }

    @Override
    public TableEntity createTable(TableEntity table) {
        return tableRepository.save(table);
    }

    @Override
    public TableEntity updateTable(Integer id, TableEntity table) {
        TableEntity old = getTableById(id);
        old.setTableName(table.getTableName());
        old.setStatus(table.getStatus());
        return tableRepository.save(old);
    }

    @Override
    public void deleteTable(Integer id) {
        tableRepository.deleteById(id);
    }

    @Override
    public TableEntity changeStatus(Integer id, String status) {
        TableEntity table = getTableById(id);
        try {
            com.viettridao.cafe.common.TableStatus st = com.viettridao.cafe.common.TableStatus.valueOf(status);
            table.setStatus(st);
        } catch (Exception e) {
            throw new IllegalArgumentException("Trạng thái bàn không hợp lệ: " + status);
        }
        return tableRepository.save(table);
    }
}
