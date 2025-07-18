package com.viettridao.cafe.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.sales.CreateReservationRequest;
import com.viettridao.cafe.dto.request.sales.MergeTableRequest;
import com.viettridao.cafe.dto.request.sales.SplitTableRequest;
import com.viettridao.cafe.dto.request.sales.MoveTableRequest;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
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
import com.viettridao.cafe.service.ReservationService;

import lombok.RequiredArgsConstructor;

/**
 * ReservationServiceImpl
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 */
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final InvoiceRepository invoiceRepository;
    private final EmployeeRepository employeeRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final MenuItemRepository menuItemRepository;

    /**
     * Tìm reservation hiện tại (chưa xóa mềm) theo tableId
     *
     * @param tableId id bàn
     * @return ReservationEntity hoặc null nếu không có
     */
    @Override
    public ReservationEntity findCurrentReservationByTableId(Integer tableId) {
        Optional<ReservationEntity> result = reservationRepository.findCurrentReservationByTableId(tableId);
        return result.orElse(null);
    }

    /**
     * Lưu đồng bộ reservation, invoice, table khi hủy bàn (xóa mềm)
     */
    @Override
    @Transactional
    public void saveReservationAndRelated(ReservationEntity reservation,
                                          InvoiceEntity invoice, TableEntity table) {
        reservationRepository.save(reservation);
        if (invoice != null) {
            invoiceRepository.save(invoice);
        }
        if (table != null) {
            tableRepository.save(table);
        }
    }

    /**
     * Tạo mới một đặt bàn.
     *
     * @param request    Đối tượng chứa thông tin cần thiết để tạo đặt bàn mới.
     * @param employeeId ID của nhân viên thực hiện đặt bàn.
     * @return Thực thể ReservationEntity vừa được tạo.
     */
    @Override
    @Transactional
    public ReservationEntity createReservation(CreateReservationRequest request, Integer employeeId) {
        // Kiểm tra trạng thái bàn
        Optional<TableEntity> tableOpt = tableRepository.findById(request.getTableId());
        if (tableOpt.isEmpty() || tableOpt.get().getStatus() != TableStatus.AVAILABLE) {
            throw new IllegalArgumentException("Bàn không tồn tại hoặc không khả dụng.");
        }

        TableEntity table = tableOpt.get();

        // Kiểm tra nhân viên tồn tại
        Optional<EmployeeEntity> employeeOpt = employeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty()) {
            throw new IllegalArgumentException("Nhân viên không tồn tại.");
        }

        EmployeeEntity employee = employeeOpt.get();

        // Tạo mới hóa đơn (InvoiceEntity)
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setStatus(InvoiceStatus.RESERVED);
        invoice.setCreatedAt(LocalDateTime.now());
        invoiceRepository.save(invoice);

        // Tạo mới đặt bàn (ReservationEntity)
        ReservationEntity reservation = new ReservationEntity();

        ReservationKey reservationKey = new ReservationKey();
        reservationKey.setIdTable(table.getId());
        reservationKey.setIdEmployee(employee.getId());
        reservationKey.setIdInvoice(invoice.getId());

        reservation.setId(reservationKey);
        reservation.setTable(table);
        reservation.setEmployee(employee);
        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerPhone(request.getCustomerPhone());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setInvoice(invoice);
        reservation.setIsDeleted(false);

        reservationRepository.save(reservation);

        // Cập nhật trạng thái bàn
        table.setStatus(TableStatus.RESERVED);
        tableRepository.save(table);

        return reservation;
    }

    /**
     * Gộp nhiều bàn OCCUPIED vào một bàn đích (cộng dồn hóa đơn, cập nhật trạng thái, xóa mềm các bàn nguồn)
     */
    @Override
    @Transactional
    public void mergeTables(MergeTableRequest request, Integer employeeId) {
        List<Integer> tableIds = request.getTableIds();
        Integer targetTableId = request.getTargetTableId();
        if (tableIds == null || tableIds.size() < 2)
            throw new IllegalArgumentException("Phải chọn ít nhất 2 bàn để gộp");
        if (!tableIds.contains(targetTableId))
            throw new IllegalArgumentException("Bàn gộp đến phải nằm trong danh sách bàn đã chọn");

        List<TableEntity> tables = tableRepository.findAllById(tableIds);
        if (tables.size() != tableIds.size())
            throw new IllegalArgumentException("Có bàn không tồn tại");
        for (TableEntity t : tables) {
            if (t.getStatus() != TableStatus.OCCUPIED)
                throw new IllegalArgumentException("Chỉ được gộp các bàn đang sử dụng (OCCUPIED)");
        }

        TableEntity targetTable = tables.stream().filter(t -> t.getId().equals(targetTableId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn gộp đến"));

        ReservationEntity targetReservation = reservationRepository.findCurrentReservationByTableId(targetTableId)
                .orElse(null);
        if (targetReservation == null)
            throw new IllegalArgumentException("Không tìm thấy reservation bàn gộp đến");
        InvoiceEntity targetInvoice = targetReservation.getInvoice();
        if (targetInvoice == null)
            throw new IllegalArgumentException("Không tìm thấy hóa đơn bàn gộp đến");

        Map<Integer, InvoiceDetailEntity> targetDetailMap = new HashMap<>();
        List<InvoiceDetailEntity> targetDetails = invoiceDetailRepository
                .findAllByInvoice_IdAndIsDeletedFalse(targetInvoice.getId());
        for (InvoiceDetailEntity detail : targetDetails) {
            targetDetailMap.put(detail.getMenuItem().getId(), detail);
        }

        for (TableEntity srcTable : tables) {
            if (srcTable.getId().equals(targetTableId))
                continue;
            ReservationEntity srcReservation = reservationRepository.findCurrentReservationByTableId(srcTable.getId())
                    .orElse(null);
            if (srcReservation == null)
                continue;
            InvoiceEntity srcInvoice = srcReservation.getInvoice();
            if (srcInvoice == null)
                continue;
            srcInvoice.setStatus(InvoiceStatus.UNDER_REVIEW);
            invoiceRepository.save(srcInvoice);

            List<InvoiceDetailEntity> srcDetails = invoiceDetailRepository
                    .findAllByInvoice_IdAndIsDeletedFalse(srcInvoice.getId());
            for (InvoiceDetailEntity srcDetail : srcDetails) {
                Integer menuItemId = srcDetail.getMenuItem().getId();
                InvoiceDetailEntity targetDetail = targetDetailMap.get(menuItemId);
                if (targetDetail != null) {
                    targetDetail.setQuantity(targetDetail.getQuantity() + srcDetail.getQuantity());
                    invoiceDetailRepository.save(targetDetail);
                } else {
                    InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                    com.viettridao.cafe.model.InvoiceKey newKey = new com.viettridao.cafe.model.InvoiceKey();
                    newKey.setIdInvoice(targetInvoice.getId());
                    newKey.setIdMenuItem(menuItemId);
                    newDetail.setId(newKey);
                    newDetail.setInvoice(targetInvoice);
                    newDetail.setMenuItem(srcDetail.getMenuItem());
                    newDetail.setQuantity(srcDetail.getQuantity());
                    newDetail.setPrice(srcDetail.getPrice());
                    newDetail.setIsDeleted(false);
                    invoiceDetailRepository.save(newDetail);
                    targetDetailMap.put(menuItemId, newDetail);
                }
                srcDetail.setIsDeleted(true);
                invoiceDetailRepository.save(srcDetail);
            }
            srcInvoice.setStatus(InvoiceStatus.CANCELLED);
            srcInvoice.setIsDeleted(true);
            invoiceRepository.save(srcInvoice);

            srcReservation.setIsDeleted(true);
            reservationRepository.save(srcReservation);

            srcTable.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(srcTable);
        }
        // Bàn đích giữ trạng thái OCCUPIED, có thể tính lại tổng tiền hóa đơn nếu muốn
    }

    /**
     * Tách bàn: chuyển một phần món từ bàn nguồn sang bàn đích
     */
    @Override
    @Transactional
    public void splitTable(SplitTableRequest request, Integer employeeId) {
        Integer sourceTableId = request.getSourceTableId();
        Integer targetTableId = request.getTargetTableId();

        if (sourceTableId.equals(targetTableId)) {
            throw new IllegalArgumentException("Bàn nguồn và bàn đích không được trùng nhau");
        }

        Optional<TableEntity> sourceTableOpt = tableRepository.findById(sourceTableId);
        Optional<TableEntity> targetTableOpt = tableRepository.findById(targetTableId);

        if (sourceTableOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy bàn nguồn");
        }
        if (targetTableOpt.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy bàn đích");
        }

        TableEntity sourceTable = sourceTableOpt.get();
        TableEntity targetTable = targetTableOpt.get();

        if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalArgumentException("Chỉ có thể tách từ bàn đang sử dụng (OCCUPIED)");
        }
        if (targetTable.getStatus() != TableStatus.AVAILABLE && targetTable.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalArgumentException("Bàn đích phải là bàn trống (AVAILABLE) hoặc đang sử dụng (OCCUPIED)");
        }

        ReservationEntity sourceReservation = reservationRepository.findCurrentReservationByTableId(sourceTableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy reservation của bàn nguồn"));
        InvoiceEntity sourceInvoice = sourceReservation.getInvoice();
        if (sourceInvoice == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn của bàn nguồn");
        }

        sourceInvoice.setStatus(InvoiceStatus.UNDER_REVIEW);
        invoiceRepository.save(sourceInvoice);

        List<InvoiceDetailEntity> sourceDetails = invoiceDetailRepository
                .findAllByInvoice_IdAndIsDeletedFalse(sourceInvoice.getId());
        Map<Integer, InvoiceDetailEntity> sourceDetailMap = new HashMap<>();
        for (InvoiceDetailEntity detail : sourceDetails) {
            sourceDetailMap.put(detail.getMenuItem().getId(), detail);
        }

        for (SplitTableRequest.SplitItemRequest item : request.getItems()) {
            Integer menuItemId = item.getMenuItemId();
            Integer splitQuantity = item.getQuantity();

            InvoiceDetailEntity sourceDetail = sourceDetailMap.get(menuItemId);
            if (sourceDetail == null) {
                throw new IllegalArgumentException("Món với ID " + menuItemId + " không có trong bàn nguồn");
            }
            if (sourceDetail.getQuantity() < splitQuantity) {
                throw new IllegalArgumentException("Số lượng tách món " + menuItemId + " vượt quá số lượng hiện có");
            }
        }

        InvoiceEntity targetInvoice;
        ReservationEntity targetReservation;
        Map<Integer, InvoiceDetailEntity> targetDetailMap = new HashMap<>();

        if (targetTable.getStatus() == TableStatus.AVAILABLE) {
            EmployeeEntity employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên"));

            targetInvoice = new InvoiceEntity();
            targetInvoice.setTotalAmount(0.0);
            targetInvoice.setCreatedAt(LocalDateTime.now());
            targetInvoice.setStatus(InvoiceStatus.PENDING_PAYMENT);
            targetInvoice.setIsDeleted(false);
            targetInvoice = invoiceRepository.save(targetInvoice);

            targetReservation = new ReservationEntity();
            ReservationKey reservationKey = new ReservationKey();
            reservationKey.setIdTable(targetTable.getId());
            reservationKey.setIdEmployee(employee.getId());
            reservationKey.setIdInvoice(targetInvoice.getId());

            targetReservation.setId(reservationKey);
            targetReservation.setTable(targetTable);
            targetReservation.setEmployee(employee);
            targetReservation.setCustomerName(sourceReservation.getCustomerName());
            targetReservation.setCustomerPhone(sourceReservation.getCustomerPhone());
            targetReservation.setReservationDate(LocalDateTime.now());
            targetReservation.setInvoice(targetInvoice);
            targetReservation.setIsDeleted(false);
            reservationRepository.save(targetReservation);

            targetTable.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(targetTable);

        } else {
            targetReservation = reservationRepository.findCurrentReservationByTableId(targetTableId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy reservation của bàn đích"));
            targetInvoice = targetReservation.getInvoice();
            if (targetInvoice == null) {
                throw new IllegalArgumentException("Không tìm thấy hóa đơn của bàn đích");
            }

            List<InvoiceDetailEntity> targetDetails = invoiceDetailRepository
                    .findAllByInvoice_IdAndIsDeletedFalse(targetInvoice.getId());
            for (InvoiceDetailEntity detail : targetDetails) {
                targetDetailMap.put(detail.getMenuItem().getId(), detail);
            }
        }

        for (SplitTableRequest.SplitItemRequest item : request.getItems()) {
            Integer menuItemId = item.getMenuItemId();
            Integer splitQuantity = item.getQuantity();

            InvoiceDetailEntity sourceDetail = sourceDetailMap.get(menuItemId);
            MenuItemEntity menuItem = sourceDetail.getMenuItem();

            InvoiceDetailEntity targetDetail = targetDetailMap.get(menuItemId);
            if (targetDetail != null) {
                targetDetail.setQuantity(targetDetail.getQuantity() + splitQuantity);
                invoiceDetailRepository.save(targetDetail);
            } else {
                InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                com.viettridao.cafe.model.InvoiceKey newKey = new com.viettridao.cafe.model.InvoiceKey();
                newKey.setIdInvoice(targetInvoice.getId());
                newKey.setIdMenuItem(menuItemId);
                newDetail.setId(newKey);
                newDetail.setInvoice(targetInvoice);
                newDetail.setMenuItem(menuItem);
                newDetail.setQuantity(splitQuantity);
                newDetail.setPrice(sourceDetail.getPrice());
                newDetail.setIsDeleted(false);
                invoiceDetailRepository.save(newDetail);
                targetDetailMap.put(menuItemId, newDetail);
            }

            int remainingQuantity = sourceDetail.getQuantity() - splitQuantity;
            if (remainingQuantity > 0) {
                sourceDetail.setQuantity(remainingQuantity);
                invoiceDetailRepository.save(sourceDetail);
            } else {
                sourceDetail.setIsDeleted(true);
                invoiceDetailRepository.save(sourceDetail);
            }
        }

        List<InvoiceDetailEntity> remainingSourceDetails = invoiceDetailRepository
                .findAllByInvoice_IdAndIsDeletedFalse(sourceInvoice.getId());

        if (remainingSourceDetails.isEmpty()) {
            sourceInvoice.setStatus(InvoiceStatus.CANCELLED);
            sourceInvoice.setIsDeleted(true);
            invoiceRepository.save(sourceInvoice);

            sourceReservation.setIsDeleted(true);
            reservationRepository.save(sourceReservation);

            sourceTable.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(sourceTable);
        } else {
            sourceInvoice.setStatus(InvoiceStatus.PENDING_PAYMENT);
            invoiceRepository.save(sourceInvoice);
        }
    }

    /**
     * Chuyển bàn: chuyển toàn bộ món từ bàn nguồn sang bàn đích
     */
    @Override
    @Transactional
    public void moveTable(MoveTableRequest request, Integer employeeId) {
        Integer sourceTableId = request.getSourceTableId();
        Integer targetTableId = request.getTargetTableId();

        if (sourceTableId.equals(targetTableId)) {
            throw new IllegalArgumentException("Bàn nguồn và bàn đích không được trùng nhau");
        }

        TableEntity sourceTable = tableRepository.findById(sourceTableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn nguồn"));
        TableEntity targetTable = tableRepository.findById(targetTableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn đích"));

        if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalArgumentException("Chỉ có thể chuyển từ bàn đang sử dụng (OCCUPIED)");
        }
        if (targetTable.getStatus() != TableStatus.AVAILABLE) {
            throw new IllegalArgumentException("Chỉ có thể chuyển sang bàn trống (AVAILABLE)");
        }

        ReservationEntity sourceReservation = reservationRepository.findCurrentReservationByTableId(sourceTableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy reservation của bàn nguồn"));
        InvoiceEntity sourceInvoice = sourceReservation.getInvoice();
        if (sourceInvoice == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn của bàn nguồn");
        }

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên thực hiện chuyển bàn"));

        ReservationKey newKey = new ReservationKey();
        newKey.setIdTable(targetTable.getId());
        newKey.setIdEmployee(employee.getId());
        newKey.setIdInvoice(sourceInvoice.getId());

        ReservationEntity targetReservation = new ReservationEntity();
        targetReservation.setId(newKey);
        targetReservation.setTable(targetTable);
        targetReservation.setEmployee(employee);
        targetReservation.setCustomerName(sourceReservation.getCustomerName());
        targetReservation.setCustomerPhone(sourceReservation.getCustomerPhone());
        targetReservation.setReservationDate(sourceReservation.getReservationDate());
        targetReservation.setInvoice(sourceInvoice);
        targetReservation.setIsDeleted(false);
        reservationRepository.save(targetReservation);

        sourceInvoice.setIsDeleted(false);
        invoiceRepository.save(sourceInvoice);

        targetTable.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(targetTable);

        sourceTable.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(sourceTable);

        sourceReservation.setIsDeleted(true);
        reservationRepository.save(sourceReservation);
        // Không cần xóa mềm invoice hay invoice detail vì đã chuyển toàn bộ sang bàn mới
    }
}