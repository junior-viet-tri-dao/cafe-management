package com.viettridao.cafe.service;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableService;
import com.viettridao.cafe.service.impl.TableServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TableServiceImplTest {
    @Mock
    private TableRepository tableRepository;

    @InjectMocks
    private TableServiceImpl tableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTables() {
        TableEntity table1 = new TableEntity();
        table1.setId(1);
        table1.setTableName("A1");
        table1.setStatus(TableStatus.AVAILABLE);
        TableEntity table2 = new TableEntity();
        table2.setId(2);
        table2.setTableName("A2");
        table2.setStatus(TableStatus.OCCUPIED);
        when(tableRepository.findAll()).thenReturn(Arrays.asList(table1, table2));
        List<TableEntity> tables = tableService.getAllTables();
        assertEquals(2, tables.size());
        assertEquals("A1", tables.get(0).getTableName());
    }

    @Test
    void testGetTableByIdFound() {
        TableEntity table = new TableEntity();
        table.setId(1);
        table.setTableName("A1");
        table.setStatus(TableStatus.AVAILABLE);
        when(tableRepository.findById(1)).thenReturn(Optional.of(table));
        TableEntity found = tableService.getTableById(1);
        assertNotNull(found);
        assertEquals("A1", found.getTableName());
    }

    @Test
    void testGetTableByIdNotFound() {
        when(tableRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> tableService.getTableById(1));
    }

    @Test
    void testCreateTable() {
        TableEntity table = new TableEntity();
        table.setTableName("A1");
        table.setStatus(TableStatus.AVAILABLE);
        TableEntity saved = new TableEntity();
        saved.setId(1);
        saved.setTableName("A1");
        saved.setStatus(TableStatus.AVAILABLE);
        when(tableRepository.save(any(TableEntity.class))).thenReturn(saved);
        TableEntity result = tableService.createTable(table);
        assertNotNull(result.getId());
        assertEquals("A1", result.getTableName());
    }

    @Test
    void testUpdateTable() {
        TableEntity existing = new TableEntity();
        existing.setId(1);
        existing.setTableName("A1");
        existing.setStatus(TableStatus.AVAILABLE);
        TableEntity update = new TableEntity();
        update.setTableName("A1-updated");
        update.setStatus(TableStatus.OCCUPIED);
        TableEntity saved = new TableEntity();
        saved.setId(1);
        saved.setTableName("A1-updated");
        saved.setStatus(TableStatus.OCCUPIED);
        when(tableRepository.findById(1)).thenReturn(Optional.of(existing));
        when(tableRepository.save(any(TableEntity.class))).thenReturn(saved);
        TableEntity result = tableService.updateTable(1, update);
        assertEquals("A1-updated", result.getTableName());
        assertEquals(TableStatus.OCCUPIED, result.getStatus());
    }

    @Test
    void testDeleteTable() {
        doNothing().when(tableRepository).deleteById(1);
        assertDoesNotThrow(() -> tableService.deleteTable(1));
        verify(tableRepository, times(1)).deleteById(1);
    }

    @Test
    void testChangeStatus() {
        TableEntity table = new TableEntity();
        table.setId(1);
        table.setTableName("A1");
        table.setStatus(TableStatus.AVAILABLE);
        when(tableRepository.findById(1)).thenReturn(Optional.of(table));
        when(tableRepository.save(any(TableEntity.class))).thenAnswer(i -> i.getArgument(0));
        TableEntity result = tableService.changeStatus(1, "OCCUPIED");
        assertEquals(TableStatus.OCCUPIED, result.getStatus());
    }

    @Test
    void testChangeStatusNotFound() {
        when(tableRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> tableService.changeStatus(1, "OCCUPIED"));
    }
}
