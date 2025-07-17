package com.viettridao.cafe.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.invoice.InvoiceDetailRequest;
import com.viettridao.cafe.dto.request.invoice.InvoiceRequest;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.InvoiceKey;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.InvoiceDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.InvoiceDetailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvoiceDetailServiceImpl implements InvoiceDetailService {

    private final TableRepository tableRepository;
    private final InvoiceRepository invoiceRepository;
    private final MenuItemRepository menuItemRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ReservationRepository reservationRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void createInvoiceDetail(InvoiceRequest request, Integer employeeId) {
        TableEntity table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found"));
        InvoiceEntity invoice = new InvoiceEntity();

        if (table.getStatus().equals(TableStatus.OCCUPIED) || table.getStatus().equals(TableStatus.RESERVED)) {
            invoice = table.getReservations().stream()
                    .filter(reservation -> Boolean.FALSE.equals(reservation.getIsDeleted()))
                    .filter(reservation -> reservation.getInvoice() != null
                            && reservation.getInvoice().getStatus() == InvoiceStatus.UNPAID)
                    .map(ReservationEntity::getInvoice)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn chưa thanh toán cho bàn này!"));
        } else {
            invoice.setTotalAmount(null);
            invoice.setCreatedAt(LocalDateTime.now());
            invoice.setStatus(InvoiceStatus.UNPAID);
            invoice.setIsDeleted(Boolean.FALSE);
            invoice.setPromotion(null);
            invoice = invoiceRepository.save(invoice);

            ReservationEntity reservation = new ReservationEntity();
            EmployeeEntity employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            reservation.setId(new ReservationKey(table.getId(), employee.getId(), invoice.getId()));
            reservation.setTable(table);
            reservation.setEmployee(employee);
            reservation.setInvoice(invoice);
            reservation.setCustomerName(null);
            reservation.setCustomerPhone(null);
            reservation.setReservationDate(LocalDateTime.now());
            reservation.setIsDeleted(Boolean.FALSE);
            reservation = reservationRepository.save(reservation);
        }

        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (InvoiceDetailRequest invoiceDetailRequest : request.getInvoiceDetails()) {
            InvoiceKey key = new InvoiceKey(invoice.getId(), invoiceDetailRequest.getMenuItemId());
            MenuItemEntity menuItemEntity = menuItemRepository.findById(invoiceDetailRequest.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));

            InvoiceDetailEntity invoiceDetail = invoiceDetailRepository.findById(key).orElse(null);
            if (invoiceDetail != null) {
                // Nếu đã có thì cộng thêm số lượng
                int newQuantity = invoiceDetail.getQuantity() + invoiceDetailRequest.getQuantity();
                invoiceDetail.setQuantity(newQuantity);
                // Nếu muốn cập nhật giá, có thể cập nhật ở đây (hoặc giữ giá cũ)
                invoiceDetailRepository.save(invoiceDetail);
            } else {
                // Nếu chưa có thì tạo mới
                invoiceDetail = new InvoiceDetailEntity();
                invoiceDetail.setId(key);
                invoiceDetail.setInvoice(invoice);
                invoiceDetail.setMenuItem(menuItemEntity);
                invoiceDetail.setQuantity(invoiceDetailRequest.getQuantity());
                invoiceDetail.setPrice(invoiceDetailRequest.getPrice());
                invoiceDetail.setIsDeleted(Boolean.FALSE);
                invoiceDetailRepository.save(invoiceDetail);
            }
            // Cộng dồn tổng tiền
            totalAmount = totalAmount.add(
                    invoiceDetailRequest.getPrice().multiply(BigDecimal.valueOf(invoiceDetailRequest.getQuantity())));
        }

        // Cập nhật tổng tiền hóa đơn
        if (invoice.getTotalAmount() == null) {
            invoice.setTotalAmount(totalAmount);
        } else {
            invoice.setTotalAmount(invoice.getTotalAmount().add(totalAmount));
        }
        invoiceRepository.save(invoice);
    }
}
