package com.viettridao.cafe.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.reservation.ReservationRequest;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;
import com.viettridao.cafe.mapper.ReservationMapper;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.ReservationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final EmployeeRepository employeeRepository;
    private final InvoiceRepository invoiceRepository;
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    /**
     * Hủy đặt bàn cho một bàn cụ thể.
     */
    @Override
    @Transactional
    public void cancelReservation(Integer tableId) {
        try {
            TableEntity table = tableRepository.findById(tableId)
                    .orElseThrow(() -> new RuntimeException("Table not found"));

            InvoiceEntity unpaidInvoice = table.getReservations().stream()
                    .filter(reservation -> Boolean.FALSE.equals(reservation.getIsDeleted()))
                    .filter(reservation -> reservation.getInvoice() != null
                            && reservation.getInvoice().getStatus() == InvoiceStatus.UNPAID)
                    .map(ReservationEntity::getInvoice)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn chưa thanh toán cho bàn này!"));

            ReservationEntity reservationEntity = reservationRepository.findByInvoice(unpaidInvoice);
            if (reservationEntity == null) {
                throw new RuntimeException("Không tìm thấy chi tiết đặt bàn cho hóa đơn này!");
            }

            // Cập nhật trạng thái bàn và hóa đơn
            table.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(table);

            unpaidInvoice.setStatus(InvoiceStatus.CANCELLED);
            invoiceRepository.save(unpaidInvoice);

            reservationRepository.delete(reservationEntity);
        } catch (Exception ex) {
            throw new RuntimeException("Hủy đặt bàn thất bại: " + ex.getMessage(), ex);
        }
    }

    /**
     * Tạo mới một đặt bàn.
     */
    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationRequest request, Integer employeeId) {
        try {
            EmployeeEntity employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            TableEntity table = tableRepository.findById(request.getTableId())
                    .orElseThrow(() -> new RuntimeException("Table not found"));

            validateTableAvailable(table);

            // Tạo hóa đơn rỗng
            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setTotalAmount(BigDecimal.ZERO);
            invoice.setCreatedAt(request.getReservationDate());
            invoice.setStatus(InvoiceStatus.UNPAID);
            invoice.setIsDeleted(false);
            invoice.setPromotion(null);
            invoice = invoiceRepository.save(invoice);

            // Tạo chi tiết đặt bàn
            ReservationEntity reservation = new ReservationEntity();
            reservation.setId(new ReservationKey(request.getTableId(), employeeId, invoice.getId()));
            reservation.setTable(table);
            reservation.setEmployee(employee);
            reservation.setInvoice(invoice);
            reservation.setCustomerName(request.getCustomerName());
            reservation.setCustomerPhone(request.getCustomerPhone());
            reservation.setReservationDate(request.getReservationDate());
            reservation.setIsDeleted(false);
            reservation = reservationRepository.save(reservation);

            // Cập nhật trạng thái bàn
            table.setStatus(TableStatus.RESERVED);
            tableRepository.save(table);

            return reservationMapper.convertToResponse(reservation);
        } catch (Exception ex) {
            throw new RuntimeException("Tạo đặt bàn thất bại: " + ex.getMessage(), ex);
        }
    }

    /**
     * Kiểm tra trạng thái bàn có phải là AVAILABLE không.
     */
    private void validateTableAvailable(TableEntity table) {
        if (table.getStatus() != TableStatus.AVAILABLE) {
            throw new RuntimeException("Chỉ có thể đặt bàn với bàn đang trống!");
        }
    }
}
