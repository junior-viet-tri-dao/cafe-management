package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.table.TableRequest;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.InvoiceDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.MenuItemService;
import com.viettridao.cafe.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final InvoiceRepository invoiceRepository;
    private final MenuItemService menuItemService;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final AccountService accountService;

    @Override
    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    @Transactional
    @Override
    public TableEntity create(TableRequest request) {
        Optional<TableEntity> table = tableRepository.findByTableName(request.getTableName());

        if(table.isPresent()) {
            throw new RuntimeException("Bàn đã có tên này rồi " + request.getTableName() );
        }

        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(request.getTableName());
        tableEntity.setStatus(TableStatus.AVAILABLE);
        tableEntity.setIsDeleted(false);

        return tableRepository.save(tableEntity);
    }

    @Override
    public TableEntity getTableById(Integer id) {
        return tableRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy bàn có id=" + id));
    }

    @Transactional
    @Override
    public void cancelTable(Integer id) {
        TableEntity table = getTableById(id);

        if (table.getStatus().equals(TableStatus.RESERVED)) {
            Optional<ReservationEntity> activeReservation = table.getReservations()
                    .stream()
                    .filter(r -> Boolean.FALSE.equals(r.getIsDeleted()))
                    .findFirst();

            if (activeReservation.isPresent()) {
                ReservationEntity reservation = activeReservation.get();
                reservation.setIsDeleted(true);
                reservationRepository.save(reservation);

                if (reservation.getInvoice() != null) {
                    reservation.getInvoice().setIsDeleted(true);
                    reservation.getInvoice().setStatus(InvoiceStatus.CANCELLED);
                    invoiceRepository.save(reservation.getInvoice());
                }
            } else {
                throw new RuntimeException("Không tìm thấy đặt bàn đang hoạt động.");
            }

            table.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(table);
        } else {
            throw new RuntimeException("Chỉ có bàn đặt trước mới được huỷ");
        }
    }

    @Transactional
    @Override
    public void selectMenusForTable(Integer tableId, List<Integer> menuIds, List<Integer> quantities) {
        if (menuIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Số lượng menu và số lượng món không khớp!");
        }

        TableEntity table = getTableById(tableId);
        ReservationEntity reservation;

        if (table.getStatus().equals(TableStatus.AVAILABLE)) {
            // 1. Tạo hóa đơn mới
            InvoiceEntity invoice = new InvoiceEntity();
            invoice.setStatus(InvoiceStatus.UNPAID);
            invoice.setIsDeleted(false);
            invoice.setTotalAmount(0.0);
            invoice.setCreatedAt(LocalDateTime.now());
            invoiceRepository.save(invoice);

            // 2. Tạo Reservation mới
            reservation = new ReservationEntity();
            ReservationKey key = new ReservationKey();
            key.setIdTable(table.getId());
            key.setIdInvoice(invoice.getId());

            // Giả sử bạn có thông tin nhân viên đang đăng nhập
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AccountEntity account = accountService.getAccountByUsername(auth.getName());

            if (account.getEmployee() == null) {
                throw new IllegalStateException("Bạn phải cập nhật thông tin nhân viên.");
            }

            key.setIdEmployee(account.getEmployee().getId());

            reservation.setId(key);
            reservation.setInvoice(invoice);
            reservation.setTable(table);
            reservation.setEmployee(account.getEmployee());
            reservation.setReservationDate(LocalDateTime.now());
            reservation.setIsDeleted(false);
            reservationRepository.save(reservation);

            // 3. Cập nhật trạng thái bàn
            table.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(table);
        } else {
            // Nếu bàn không trống thì lấy Reservation hiện tại
            Optional<ReservationEntity> reservationOpt = reservationRepository.findLatestReservation(table.getId());
            if (!reservationOpt.isPresent()) {
                throw new RuntimeException("Bàn chưa có chi tiết đặt bàn");
            }
            reservation = reservationOpt.get();
        }

        InvoiceEntity invoice = reservation.getInvoice();
        if (invoice == null) {
            throw new RuntimeException("Bàn chưa có hóa đơn");
        }

        // 4. Thêm món
        for (int i = 0; i < menuIds.size(); i++) {
            Integer menuId = menuIds.get(i);
            Integer quantity = quantities.get(i);

            if (quantity <= 0) continue;

            MenuItemEntity menuItem = menuItemService.getMenuItemById(menuId);
            InvoiceKey key = new InvoiceKey(invoice.getId(), menuItem.getId());

            Optional<InvoiceDetailEntity> existingDetailOpt = invoiceDetailRepository.findById(key);

            if (existingDetailOpt.isPresent()) {
                // Món đã tồn tại => cộng dồn
                InvoiceDetailEntity existingDetail = existingDetailOpt.get();
                existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
                invoiceDetailRepository.save(existingDetail);
            } else {
                // Món mới => thêm mới
                InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                newDetail.setId(key);
                newDetail.setMenuItem(menuItem);
                newDetail.setQuantity(quantity);
                newDetail.setInvoice(invoice);
                newDetail.setPrice(menuItem.getCurrentPrice());
                invoiceDetailRepository.save(newDetail);
            }
        }
        double totalAmount = invoiceDetailRepository
                .findAllByInvoice_Id(invoice.getId())
                .stream()
                .mapToDouble(detail -> detail.getQuantity() * detail.getPrice())
                .sum();

        invoice.setTotalAmount(totalAmount);
        invoiceRepository.save(invoice);

        table.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(table);
    }
}
