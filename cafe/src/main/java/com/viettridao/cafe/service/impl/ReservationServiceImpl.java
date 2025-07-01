package com.viettridao.cafe.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final EmployeeRepository employeeRepository;
    private final InvoiceRepository invoiceRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public TableBookingResponse bookTable(TableBookingRequest request) {
        // 1. Tìm bàn
        TableEntity table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

        // 2. Kiểm tra trạng thái bàn
        if (table.getStatus() == TableStatus.RESERVED) {
            return new TableBookingResponse(false, "❌ Bàn đã được đặt trước.");
        } else if (table.getStatus() == TableStatus.OCCUPIED) {
            return new TableBookingResponse(false, "❌ Bàn đang được sử dụng.");
        }

        // 3. Kiểm tra trùng lịch đặt
        boolean exists = reservationRepository.existsByTableIdAndReservationDateAndReservationTimeAndIsDeletedFalse(
                request.getTableId(), request.getReservationDate(), request.getReservationTime());
        if (exists) {
            return new TableBookingResponse(false, "❌ Bàn đã có người đặt tại thời điểm này.");
        }

        // 4. Lấy nhân viên
        EmployeeEntity employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với ID: " + request.getEmployeeId()));

        // 5. Tạo hóa đơn trước để lấy ID
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setTotalAmount(0.0);
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setIsDeleted(false);
        invoice = invoiceRepository.save(invoice);

        // 6. Tạo đối tượng đặt bàn
        ReservationEntity reservation = new ReservationEntity();
        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerPhone(request.getCustomerPhone());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setReservationTime(request.getReservationTime());
        reservation.setIsDeleted(false);

        // 7. Gán các entity trước khi gán khóa phức hợp
        reservation.setTable(table);
        reservation.setEmployee(employee);
        reservation.setInvoice(invoice);

        ReservationKey key = new ReservationKey();
        key.setIdTable(table.getId());
        key.setIdEmployee(employee.getId());
        key.setIdInvoice(invoice.getId());
        reservation.setId(key);

        // 8. Lưu thông tin đặt bàn
        reservationRepository.save(reservation);

        // 9. Cập nhật trạng thái bàn → đã đặt trước
        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);

        // 10. Trả kết quả
        return new TableBookingResponse(true,
                "✅ Đặt bàn thành công! Mã hóa đơn #" + invoice.getId() +
                " cho khách " + request.getCustomerName());
    }
}
