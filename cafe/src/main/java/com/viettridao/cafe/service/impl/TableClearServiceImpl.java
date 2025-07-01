package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableClearService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableClearServiceImpl implements TableClearService {

    private final TableRepository tableRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    @Transactional
    @Override
    public void clearTable(Integer tableId) {
        TableEntity table = tableRepository.findById(tableId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn ID: " + tableId));

        // ❌ Chặn nếu đang phục vụ
        if (table.getStatus() == TableStatus.OCCUPIED) {
            throw new RuntimeException("Không thể hủy bàn đang phục vụ (OCCUPIED).");
        }

        // ✅ Tìm hóa đơn chưa thanh toán
        InvoiceEntity invoice = invoiceRepository
            .findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(
                tableId, com.viettridao.cafe.common.InvoiceStatus.UNPAID);

        if (invoice != null) {
            // ✅ Xóa mềm các món ăn
            List<InvoiceDetailEntity> details = invoice.getInvoiceDetails();
            if (details != null && !details.isEmpty()) {
                details.forEach(detail -> detail.setIsDeleted(true));
                invoiceDetailRepository.saveAll(details);
            }

            // ✅ Xóa mềm các reservation liên quan
            if (invoice.getReservations() != null) {
                invoice.getReservations().forEach(reservation -> {
                    reservation.setIsDeleted(true);
                });
            }

            // ✅ Xóa mềm hóa đơn
            invoice.setTotalAmount(0.0);
            invoice.setIsDeleted(true);
            invoiceRepository.save(invoice);
        }

        // ✅ Cập nhật trạng thái bàn
        table.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(table);
    }
}
