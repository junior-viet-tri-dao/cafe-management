package com.viettridao.cafe.service.invoice;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.TableRepository;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements IInvoiceService {

    private final TableRepository tableRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public Optional<InvoiceEntity> findByTableId(Integer tableId) {
        return invoiceRepository.findInvoiceByTableId(tableId);
    }

    @Override
    @Transactional
    public void payment(Integer tableId) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

        if (!table.getStatus().equals(TableStatus.OCCUPIED)) {
            throw new RuntimeException("Chỉ có bàn đang sử dụng mới được thanh toán");
        }

        InvoiceEntity invoice = invoiceRepository.findInvoiceByTableIdForPay(tableId);

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new RuntimeException("Hóa đơn đã thanh toán rồi!");
        }

        invoice.setStatus(InvoiceStatus.PAID);
        table.setStatus(TableStatus.AVAILABLE);

        invoiceRepository.save(invoice);
        tableRepository.save(table);
    }
}
