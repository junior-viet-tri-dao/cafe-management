package com.viettridao.cafe.service.reservation;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;
import com.viettridao.cafe.mapper.ReservationMapper;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RerservationServiceImpl implements IReservationService{

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final EmployeeRepository employeeRepository;
    private final InvoiceRepository invoiceRepository;
    private final TableRepository tableRepository;

    @Override
    public List<ReservationResponse> getAllReservation() {
        return reservationRepository.findAllByDeletedFalse()
                .stream()
                .map(reservationMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public void createReservation(ReservationCreateRequest request) {
        try {
            String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            EmployeeEntity employee = employeeRepository.findByAccount_Username(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên từ username"));

            TableEntity table = tableRepository.findById(request.getTableId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

            if (table.getStatus() != TableStatus.AVAILABLE) {
                throw new RuntimeException("Chỉ có thể đặt bàn với bàn đang trống!");
            }

            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setTotalAmount(0.0);
            invoice.setCreatedAt(request.getReservationDate());
            invoice.setStatus(InvoiceStatus.PENDING_PAYMENT);
            invoice.setDeleted(false);
            invoice.setPromotion(null);
            invoice = invoiceRepository.save(invoice);

            ReservationEntity reservation = new ReservationEntity();
            reservation.setId(new ReservationKey(request.getTableId(), employee.getId(), invoice.getId()));
            reservation.setTable(table);
            reservation.setEmployee(employee);
            reservation.setInvoice(invoice);
            reservation.setCustomerName(request.getCustomerName());
            reservation.setCustomerPhone(request.getCustomerPhone());
            reservation.setReservationDate(request.getReservationDate());
            reservation.setDeleted(false);
            reservationRepository.save(reservation);

            table.setStatus(TableStatus.RESERVED);
            tableRepository.save(table);

            System.out.println("Username: " + username);
            System.out.println("Employee: " + employee);
            System.out.println("Invoice ID: " + invoice.getId());


        } catch (Exception ex) {
            throw new RuntimeException("Tạo đặt bàn thất bại: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void deleteReservation(Integer invoiceId) {
        ReservationEntity reservationEntity = findReservationOrThrow(invoiceId);
        reservationEntity.setDeleted(true);
        reservationRepository.save(reservationEntity);

        InvoiceEntity invoiceEntity = reservationEntity.getInvoice();
        if (invoiceEntity != null) {
            invoiceEntity.setDeleted(true);
            invoiceEntity.setStatus(InvoiceStatus.CANCELLED);
            invoiceRepository.save(invoiceEntity);
        }

        TableEntity tableEntity = reservationEntity.getTable();
        if (tableEntity.getStatus() == TableStatus.RESERVED) {
            tableEntity.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(tableEntity);
        }
    }

    //Tìm thông tin đặt bàn thông qua id hóa đơn
    private ReservationEntity findReservationOrThrow(Integer invoiceId) {
        return reservationRepository.findByInvoice_Id(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin đặt bàn với mã hóa đơn " + invoiceId));
    }
}
