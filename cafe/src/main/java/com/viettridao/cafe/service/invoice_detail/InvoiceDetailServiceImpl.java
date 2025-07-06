package com.viettridao.cafe.service.invoice_detail;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.invoice.InvoiceRequest;
import com.viettridao.cafe.dto.request.invoicedetail.InvoiceDetailRequest;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.*;

@Service
@RequiredArgsConstructor
public class InvoiceDetailServiceImpl implements IInvoiceDetailService {

    private final TableRepository tableRepository;
    private final InvoiceRepository invoiceRepository;
    private final MenuItemRepository menuItemRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void createMenusForInvoice(InvoiceRequest request) {
        TableEntity table = getTable(request.getTableId());

        InvoiceEntity invoice = findOrCreateInvoice(table);
        addOrUpdateInvoiceDetails(invoice, request.getInvoiceDetails());
        updateInvoiceTotal(invoice);

        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);
    }

    private TableEntity getTable(Integer tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với ID: " + tableId));
    }

    private InvoiceEntity findOrCreateInvoice(TableEntity table) {
        if (table.getStatus() == TableStatus.OCCUPIED || table.getStatus() == TableStatus.RESERVED) {
            return table.getReservations().stream()
                    .filter(res -> Boolean.FALSE.equals(res.getDeleted()))
                    .map(ReservationEntity::getInvoice)
                    .filter(inv -> inv.getStatus() == InvoiceStatus.PENDING_PAYMENT)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn đang chờ thanh toán."));
        }

        // Tạo mới nếu bàn trống
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setStatus(InvoiceStatus.PENDING_PAYMENT);
        invoice.setTotalAmount(0.00);
        invoice.setDeleted(false);
        invoice = invoiceRepository.save(invoice);

        ReservationEntity reservation = createReservation(table, invoice);
        reservationRepository.save(reservation);

        return invoice;
    }

    private ReservationEntity createReservation(TableEntity table, InvoiceEntity invoice) {
        EmployeeEntity employee = getCurrentEmployee();
        ReservationEntity reservation = new ReservationEntity();

        ReservationKey key = new ReservationKey(table.getId(), employee.getId(), invoice.getId());
        reservation.setId(key);
        reservation.setTable(table);
        reservation.setEmployee(employee);
        reservation.setInvoice(invoice);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setDeleted(false);
        reservation.setCustomerName("Khách vãng lai");
        reservation.setCustomerPhone(null);

        return reservation;
    }

    private EmployeeEntity getCurrentEmployee() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountEntity account = accountRepository.getAccountByUsername(auth.getName());

        if (account == null || account.getEmployee() == null) {
            throw new IllegalStateException("Không thể lấy thông tin nhân viên hiện tại.");
        }

        return account.getEmployee();
    }

    private void addOrUpdateInvoiceDetails(InvoiceEntity invoice, List<InvoiceDetailRequest> requests) {
        for (InvoiceDetailRequest req : requests) {
            if (req.getQuantity() <= 0) continue;

            MenuItemEntity menuItem = menuItemRepository.findById(req.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món: " + req.getMenuItemId()));

            InvoiceKey key = new InvoiceKey(invoice.getId(), menuItem.getId());

            InvoiceDetailEntity detail = invoiceDetailRepository.findById(key).orElse(null);
            if (detail == null) {
                detail = new InvoiceDetailEntity();
                detail.setId(key);
                detail.setInvoice(invoice);
                detail.setMenuItem(menuItem);
                detail.setPrice(req.getPrice());
                detail.setQuantity(req.getQuantity());
                detail.setDeleted(false);
            } else {
                detail.setQuantity(detail.getQuantity() + req.getQuantity());
            }

            invoiceDetailRepository.save(detail);
        }
    }

    private void updateInvoiceTotal(InvoiceEntity invoice) {
        double total = invoiceDetailRepository.findByInvoiceId(invoice.getId())
                .stream()
                .mapToDouble(detail -> detail.getQuantity() * detail.getPrice())
                .sum();

        invoice.setTotalAmount(total);
        invoiceRepository.save(invoice);
    }

}
