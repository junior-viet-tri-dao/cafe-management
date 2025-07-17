package com.viettridao.cafe.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.response.invoice.InvoiceResponse;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.InvoiceService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

        private final TableRepository tableRepository;
        private final InvoiceRepository invoiceRepository;

        @Override
        public InvoiceEntity getByTableId(Integer tableId) {
                TableEntity table = tableRepository.findById(tableId)
                                .orElseThrow(() -> new RuntimeException("Table not found"));
                return table.getReservations().stream()
                                .filter(reservation -> Boolean.FALSE.equals(reservation.getIsDeleted()))
                                .filter(reservation -> reservation.getInvoice() != null
                                                && reservation.getInvoice().getStatus() == InvoiceStatus.UNPAID)
                                .map(ReservationEntity::getInvoice)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException(
                                                "Không tìm thấy hóa đơn chưa thanh toán cho bàn này!"));
        }

        @Override
        public void checkout(Integer tableId) {
                InvoiceEntity invoice = getByTableId(tableId);
                invoice.setStatus(InvoiceStatus.PAID);

                TableEntity table = tableRepository.findById(tableId)
                                .orElseThrow(() -> new RuntimeException("Table not found"));
                table.setStatus(TableStatus.AVAILABLE);
                tableRepository.save(table);
                // Lưu lại thay đổi vào database
                invoiceRepository.save(invoice);
        }

        @Override
        public List<InvoiceEntity> getAllByInvoiceDateBetween(LocalDateTime start, LocalDateTime end) {
                return invoiceRepository.findAllByInvoiceDateBetween(start, end);

        }

}
