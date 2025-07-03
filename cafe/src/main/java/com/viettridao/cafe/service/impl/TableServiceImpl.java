package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.table.TableRequest;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;

    @Override
    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    @Transactional
    @Override
    public TableEntity create(TableRequest request) {
        Optional<TableEntity> table = tableRepository.findByTableName(request.getTableName());

        if(table.isPresent()) {
            throw new RuntimeException("Bàn đã có tên này rồi " + request.getTableName() );
        }

        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(request.getTableName());
        tableEntity.setStatus(TableStatus.AVAILABLE);
        tableEntity.setIsDeleted(false);

        return tableRepository.save(tableEntity);
    }

    @Override
    public TableEntity getTableById(Integer id) {
        return tableRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy bàn có id=" + id));
    }
}
