package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.ReservationService;
import com.viettridao.cafe.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TableService tableService;
    private final AccountService accountService;
    private final InvoiceRepository invoiceRepository;
    private final TableRepository tableRepository;

    @Transactional
    @Override
    public ReservationEntity createReservation(ReservationCreateRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Bạn cần đăng nhập để đặt bàn");
        }

        TableEntity table = tableService.getTableById(request.getTableId());
        AccountEntity account = accountService.getAccountByUsername(auth.getName());

        if(account.getEmployee() == null) {
            throw new IllegalStateException("Bạn phải cập nhật đầy đủ thông tin cá nhân");
        }

        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoice.setIsDeleted(false);
        invoice.setTotalAmount(0.0);

        InvoiceEntity savedInvoice = invoiceRepository.save(invoice);

        ReservationEntity reservation = new ReservationEntity();
        ReservationKey key = new ReservationKey();
        key.setIdTable(table.getId());
        key.setIdEmployee(account.getEmployee().getId());
        key.setIdInvoice(savedInvoice.getId());
        reservation.setId(key);

        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerPhone(request.getCustomerPhone());
        reservation.setIsDeleted(false);
        reservation.setEmployee(account.getEmployee());
        reservation.setInvoice(savedInvoice);
        reservation.setTable(table);
        reservation.setReservationDate(request.getReservationDate());

        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);

        return reservationRepository.save(reservation);
    }

}
