package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.table.CreateTableRequest;
import com.viettridao.cafe.dto.request.table.SplitItemRequest;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.*;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.MenuItemService;
import com.viettridao.cafe.service.TableService;
import com.viettridao.cafe.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final AccountService accountService;
    private final MenuItemService menuItemService;
    private  final InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public List<TableEntity> getAllTables() {
        return tableRepository.findAll();
    }

    @Transactional
    @Override
    public TableEntity createTable(CreateTableRequest request) {
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
    public TableEntity getTableById(Integer tableId) {
        return  tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với id = " + tableId));
    }

    @Transactional
    @Override
    public void cancelTable(Integer tableId) {
        TableEntity table = getTableById(tableId);

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
            Optional<ReservationEntity> reservationOpt = reservationRepository.findTopByTableIdAndIsDeletedOrderByReservationDateDesc(table.getId(), false);
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
                existingDetail.setIsDeleted(false);
                invoiceDetailRepository.save(existingDetail);
            } else {
                // Món mới => thêm mới
                InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                newDetail.setId(key);
                newDetail.setMenuItem(menuItem);
                newDetail.setQuantity(quantity);
                newDetail.setInvoice(invoice);
                newDetail.setPrice(menuItem.getCurrentPrice());
                newDetail.setIsDeleted(false);
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

    @Transactional
    @Override
    public void payment(Integer tableId) {
        TableEntity table = getTableById(tableId);

        if(!table.getStatus().equals(TableStatus.OCCUPIED)) {
            throw new RuntimeException("Chỉ có bàn đang sử dụng mới được thanh toán");
        }
        Optional<ReservationEntity> reservationOpt = reservationRepository.findTopByTableIdAndIsDeletedOrderByReservationDateDesc(table.getId(), false);

        if(reservationOpt.isPresent()) {
            ReservationEntity reservation = reservationOpt.get();

            if(reservation.getInvoice() != null) {
                InvoiceEntity invoice = reservation.getInvoice();
                invoice.setStatus(InvoiceStatus.PAID);

                invoiceRepository.save(invoice);
            }

            reservation.setIsDeleted(true);
            reservationRepository.save(reservation);
            table.setStatus(TableStatus.AVAILABLE);
        }
    }

    @Transactional
    @Override
    public void moveTable(Integer fromTableId, Integer toTableId) {
        TableEntity tableFrom = getTableById(fromTableId);
        TableEntity tableTo = getTableById(toTableId);

        if(tableFrom.getStatus().equals(TableStatus.AVAILABLE)) {
            throw new RuntimeException("Bàn cần chuyển có trạng thái là đang sử dụng hoặc bàn đặt trước");
        }

        if(!tableTo.getStatus().equals(TableStatus.AVAILABLE)) {
            throw new RuntimeException("Bàn chuyển đến phải là bàn đang trạng thái trống");
        }

        Optional<ReservationEntity> reservationOpt = reservationRepository.
                findTopByTableIdAndIsDeletedOrderByReservationDateDesc(tableFrom.getId(), false);

        if(reservationOpt.isPresent()) {
            ReservationEntity reservation = reservationOpt.get();

            InvoiceEntity invoice = reservation.getInvoice();
            EmployeeEntity employee = reservation.getEmployee();

            ReservationKey key = new ReservationKey();
            key.setIdTable(tableTo.getId());
            key.setIdInvoice(invoice.getId());
            key.setIdEmployee(employee.getId());

            ReservationEntity newReservation = new ReservationEntity();

            newReservation.setId(key);
            newReservation.setInvoice(invoice);
            newReservation.setEmployee(employee);
            newReservation.setTable(tableTo);

            if (reservation.getCustomerPhone() != null) {
                newReservation.setCustomerPhone(reservation.getCustomerPhone());
            }
            if (reservation.getCustomerName() != null) {
                newReservation.setCustomerName(reservation.getCustomerName());
            }
            if (reservation.getReservationDate() != null) {
                newReservation.setReservationDate(reservation.getReservationDate());
            }

            if(tableFrom.getStatus().equals(TableStatus.RESERVED)) {
                tableTo.setStatus(TableStatus.RESERVED);
            }else if(tableFrom.getStatus().equals(TableStatus.OCCUPIED)) {
                newReservation.setIsDeleted(false);
                tableTo.setStatus(TableStatus.OCCUPIED);
            }

            reservationRepository.save(newReservation);

            tableFrom.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(tableFrom);
            tableRepository.save(tableTo);

            reservation.setIsDeleted(true);
            reservationRepository.save(reservation);
        }else {
            throw new RuntimeException("Bàn cần chuyển chưa có chi tiết đặt bàn");
        }

    }

    @Transactional
    @Override
    public void merge(List<Integer> sourceTableIds, Integer targetTableId) {
        if (sourceTableIds == null || sourceTableIds.isEmpty() || targetTableId == null) {
            throw new IllegalArgumentException("Thiếu thông tin bàn gộp.");
        }

        if (sourceTableIds.contains(targetTableId)) {
            sourceTableIds.remove(targetTableId);
        }

        TableEntity tableTarget = getTableById(targetTableId);
        InvoiceEntity mainInvoice = null;

        // Lấy hóa đơn chính từ bàn đích
        Optional<ReservationEntity> targetReservation = reservationRepository
                .findTopByTableIdAndIsDeletedOrderByReservationDateDesc(targetTableId, false);
        if (targetReservation.isPresent()) {
            mainInvoice = targetReservation.get().getInvoice();
        }

        // Nếu chưa có hóa đơn => tạo mới
        if (mainInvoice == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            AccountEntity account = accountService.getAccountByUsername(auth.getName());

            if (account.getEmployee() == null) {
                throw new IllegalStateException("Bạn phải cập nhật đầy đủ thông tin cá nhân");
            }

            mainInvoice = new InvoiceEntity();
            mainInvoice.setStatus(InvoiceStatus.UNPAID);
            mainInvoice.setIsDeleted(false);
            mainInvoice.setTotalAmount(0.0);
            mainInvoice.setCreatedAt(LocalDateTime.now());
            mainInvoice.setInvoiceDetails(new ArrayList<>());
            invoiceRepository.save(mainInvoice);

            // Tạo reservation cho bàn đích
            ReservationEntity newReservation = new ReservationEntity();
            ReservationKey key = new ReservationKey();
            key.setIdTable(tableTarget.getId());
            key.setIdEmployee(account.getEmployee().getId());
            key.setIdInvoice(mainInvoice.getId());

            newReservation.setId(key);
            newReservation.setInvoice(mainInvoice);
            newReservation.setEmployee(account.getEmployee());
            newReservation.setTable(tableTarget);
            newReservation.setIsDeleted(false);

            reservationRepository.save(newReservation);
        }

        double totalAmount = 0.0;

        // Gộp từ các bàn nguồn
        for (Integer sourceId : sourceTableIds) {
            Optional<ReservationEntity> sourceOpt = reservationRepository
                    .findTopByTableIdAndIsDeletedOrderByReservationDateDesc(sourceId, false);
            if (!sourceOpt.isPresent()) continue;

            ReservationEntity sourceReservation = sourceOpt.get();
            InvoiceEntity sourceInvoice = sourceReservation.getInvoice();

            if (sourceInvoice == null) continue;

            // Lấy món từ source và merge vào mainInvoice
            List<InvoiceDetailEntity> sourceItems = invoiceDetailRepository.findByInvoice_Id(sourceInvoice.getId());
            for (InvoiceDetailEntity sourceItem : sourceItems) {
                totalAmount += mergeItemToMainInvoice(mainInvoice, sourceItem);
            }

            // Nếu khác invoice chính thì xóa
            if (!sourceInvoice.getId().equals(mainInvoice.getId())) {
                sourceInvoice.setIsDeleted(true);
                invoiceRepository.save(sourceInvoice);
            }

            // Cập nhật bàn source thành AVAILABLE
            TableEntity sourceTable = sourceReservation.getTable();
            if (sourceTable != null) {
                sourceTable.setStatus(TableStatus.AVAILABLE);
                tableRepository.save(sourceTable);
            }

            sourceReservation.setIsDeleted(true);
            reservationRepository.save(sourceReservation);
        }

        mainInvoice.setTotalAmount(totalAmount);
        invoiceRepository.save(mainInvoice);

        // Đảm bảo bàn đích là OCCUPIED
        tableTarget.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(tableTarget);
    }

    @Transactional
    @Override
    public void splitTable(Integer fromTableId, Integer toTableId, List<SplitItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Không có món nào để tách.");
        }

        TableEntity fromTable = getTableById(fromTableId);
        TableEntity toTable = getTableById(toTableId);

        if (!fromTable.getStatus().equals(TableStatus.OCCUPIED)) {
            throw new RuntimeException("Bàn nguồn phải đang sử dụng.");
        }

        if (!toTable.getStatus().equals(TableStatus.AVAILABLE)) {
            throw new RuntimeException("Bàn đích phải đang trống.");
        }

        Optional<ReservationEntity> fromReservationOpt = reservationRepository
                .findTopByTableIdAndIsDeletedOrderByReservationDateDesc(fromTableId, false);

        if (!fromReservationOpt.isPresent()) {
            throw new RuntimeException("Bàn nguồn không có thông tin đặt bàn.");
        }

        ReservationEntity fromReservation = fromReservationOpt.get();
        InvoiceEntity fromInvoice = fromReservation.getInvoice();

        if (fromInvoice == null) {
            throw new RuntimeException("Bàn nguồn không có hóa đơn.");
        }

        InvoiceEntity toInvoice = new InvoiceEntity();
        toInvoice.setStatus(InvoiceStatus.UNPAID);
        toInvoice.setIsDeleted(false);
        toInvoice.setTotalAmount(0.0);
        toInvoice.setCreatedAt(LocalDateTime.now());
        invoiceRepository.save(toInvoice);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountEntity account = accountService.getAccountByUsername(auth.getName());

        if (account.getEmployee() == null) {
            throw new IllegalStateException("Bạn phải cập nhật thông tin nhân viên.");
        }

        ReservationEntity toReservation = new ReservationEntity();
        ReservationKey toKey = new ReservationKey();
        toKey.setIdTable(toTable.getId());
        toKey.setIdInvoice(toInvoice.getId());
        toKey.setIdEmployee(account.getEmployee().getId());

        toReservation.setId(toKey);
        toReservation.setInvoice(toInvoice);
        toReservation.setEmployee(account.getEmployee());
        toReservation.setTable(toTable);
        toReservation.setReservationDate(LocalDateTime.now());
        toReservation.setIsDeleted(false);

        reservationRepository.save(toReservation);

        double fromTotal = 0.0;
        double toTotal = 0.0;

        // Tách từng món
        for (SplitItemRequest splitItem : items) {
            Integer itemId = splitItem.getItemId();
            Integer quantityToSplit = splitItem.getQuantity();

            if (quantityToSplit <= 0) continue;

            InvoiceKey fromKey = new InvoiceKey(fromInvoice.getId(), itemId);
            Optional<InvoiceDetailEntity> fromDetailOpt = invoiceDetailRepository.findById(fromKey);

            if (!fromDetailOpt.isPresent()) {
                throw new RuntimeException("Không tìm thấy món cần tách trong hóa đơn bàn nguồn.");
            }

            InvoiceDetailEntity fromDetail = fromDetailOpt.get();

            if (fromDetail.getQuantity() < quantityToSplit) {
                throw new RuntimeException("Số lượng tách vượt quá số lượng hiện có.");
            }

            fromDetail.setQuantity(fromDetail.getQuantity() - quantityToSplit);
            if (fromDetail.getQuantity() == 0) {
                fromDetail.setIsDeleted(true);
            }
            invoiceDetailRepository.save(fromDetail);

            // Thêm vào hóa đơn bàn đích
            InvoiceKey toKeyItem = new InvoiceKey(toInvoice.getId(), itemId);
            Optional<InvoiceDetailEntity> toDetailOpt = invoiceDetailRepository.findById(toKeyItem);

            if (toDetailOpt.isPresent()) {
                InvoiceDetailEntity toDetail = toDetailOpt.get();
                toDetail.setQuantity(toDetail.getQuantity() + quantityToSplit);
                invoiceDetailRepository.save(toDetail);
            } else {
                InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                newDetail.setId(toKeyItem);
                newDetail.setInvoice(toInvoice);
                newDetail.setMenuItem(fromDetail.getMenuItem());
                newDetail.setQuantity(quantityToSplit);
                newDetail.setPrice(fromDetail.getPrice());
                newDetail.setIsDeleted(false);
                invoiceDetailRepository.save(newDetail);
            }

            // Cộng tiền
            double price = fromDetail.getPrice();
            fromTotal += (fromDetail.getQuantity()) * price;
            toTotal += quantityToSplit * price;
        }

        // Cập nhật tổng tiền
        double newFromTotal = invoiceDetailRepository
                .findAllByInvoice_Id(fromInvoice.getId())
                .stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .mapToDouble(d -> d.getQuantity() * d.getPrice())
                .sum();
        fromInvoice.setTotalAmount(newFromTotal);
        invoiceRepository.save(fromInvoice);

        double newToTotal = invoiceDetailRepository
                .findAllByInvoice_Id(toInvoice.getId())
                .stream()
                .filter(d -> !Boolean.TRUE.equals(d.getIsDeleted()))
                .mapToDouble(d -> d.getQuantity() * d.getPrice())
                .sum();
        toInvoice.setTotalAmount(newToTotal);
        invoiceRepository.save(toInvoice);

        toTable.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(toTable);
    }

    private double mergeItemToMainInvoice(InvoiceEntity mainInvoice, InvoiceDetailEntity sourceItem) {
        if (mainInvoice.getInvoiceDetails() == null) {
            mainInvoice.setInvoiceDetails(new ArrayList<>());
        }

        for (InvoiceDetailEntity item : mainInvoice.getInvoiceDetails()) {
            if (item.getMenuItem().getId().equals(sourceItem.getMenuItem().getId())) {
                item.setQuantity(item.getQuantity() + sourceItem.getQuantity());
                return sourceItem.getQuantity() * sourceItem.getPrice();
            }
        }

        InvoiceDetailEntity newItem = new InvoiceDetailEntity();
        InvoiceKey newKey = new InvoiceKey(mainInvoice.getId(), sourceItem.getMenuItem().getId());

        newItem.setId(newKey);
        newItem.setInvoice(mainInvoice);
        newItem.setMenuItem(sourceItem.getMenuItem());
        newItem.setQuantity(sourceItem.getQuantity());
        newItem.setPrice(sourceItem.getPrice());
        newItem.setIsDeleted(false);

        mainInvoice.getInvoiceDetails().add(newItem);
        return newItem.getQuantity() * newItem.getPrice();
    }
}
