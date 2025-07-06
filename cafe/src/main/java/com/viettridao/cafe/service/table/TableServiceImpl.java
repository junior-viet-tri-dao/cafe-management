package com.viettridao.cafe.service.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.mapper.TableMapper;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.repository.*;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements ITableService {

    private final TableRepository tableRepository;
    private final TableMapper tableMapper;
    private final ReservationRepository reservationRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public List<TableResponse> getAllTables() {
        return tableRepository.findAllByDeletedFalse()
                .stream()
                .map(tableMapper::toResponse)
                .toList();
    }

    @Override
    public TableEntity getTableById(Integer tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với ID: " + tableId));
    }

    @Override
    @Transactional
    public TableResponse switchTable(Integer sourceTableId, Integer targetTableId) {

        if(sourceTableId.equals(targetTableId))
            throw new RuntimeException("Không thể chuyển vào chính bàn hiện tại!");

        TableEntity sourceTable = findTableOrThrow(sourceTableId);
        TableEntity targetTable = findTableOrThrow(targetTableId);

        if(!(targetTable.getStatus().equals(TableStatus.AVAILABLE)))
            throw new RuntimeException("Chỉ có thể chuyển đến bàn đang trống!");

        ReservationEntity sourceReservation = findActiveReservationForTable(sourceTable);

        ReservationEntity newReservation = new ReservationEntity(
                new ReservationKey(targetTable.getId(), sourceReservation.getEmployee().getId(), sourceReservation.getInvoice().getId()),
                targetTable,
                sourceReservation.getEmployee(),
                sourceReservation.getInvoice(),
                sourceReservation.getCustomerName(),
                sourceReservation.getCustomerPhone(),
                sourceReservation.getReservationDate(),
                false
        );

        reservationRepository.save(newReservation);
        sourceTable.setDeleted(false);

        if(sourceTable.getStatus().equals(TableStatus.RESERVED)) {
            targetTable.setStatus(TableStatus.RESERVED);
        }else if(sourceTable.getStatus().equals(TableStatus.OCCUPIED)) {
            targetTable.setStatus(TableStatus.OCCUPIED);
        }
        sourceTable.setStatus(TableStatus.AVAILABLE);


        tableRepository.save(sourceTable);
        TableEntity updatedTargetTable = tableRepository.save(targetTable);

        return tableMapper.toResponse(updatedTargetTable);
    }

    @Override
    @Transactional
    public void splitTable(Integer sourceTableId, Integer targetTableId, Map<Integer, Integer> menuItemIdToQuantity) {
        if (sourceTableId.equals(targetTableId)) {
            throw new RuntimeException("Không thể tách sang chính bàn đó!");
        }

        TableEntity sourceTable = findTableOrThrow(sourceTableId);
        TableEntity targetTable = findTableOrThrow(targetTableId);

        // Tìm hóa đơn bàn nguồn
        ReservationEntity sourceReservation = findActiveReservationForTable(sourceTable);
        InvoiceEntity sourceInvoice = sourceReservation.getInvoice();

        // Tìm hóa đơn bàn đích (nếu có)
        InvoiceEntity targetInvoice = invoiceRepository
                .findAllByReservations_Table_IdAndStatusInAndDeletedFalse(
                        targetTableId, List.of(InvoiceStatus.PENDING_PAYMENT, InvoiceStatus.RESERVED)
                ).stream().findFirst()
                .orElseGet(() -> {
                    // Nếu không có thì tạo mới
                    InvoiceEntity newInvoice = createPendingInvoice();

                    ReservationEntity targetReservation = new ReservationEntity(
                            new ReservationKey(targetTableId, sourceReservation.getEmployee().getId(), newInvoice.getId()),
                            targetTable,
                            sourceReservation.getEmployee(),
                            newInvoice,
                            "Khách vãng lai",
                            null,
                            LocalDateTime.now(),
                            false
                    );
                    reservationRepository.save(targetReservation);
                    return newInvoice;
                });

        // Tách món từ hóa đơn nguồn sang hóa đơn đích
        for (Map.Entry<Integer, Integer> entry : menuItemIdToQuantity.entrySet()) {
            Integer menuItemId = entry.getKey();
            Integer quantity = entry.getValue();

            if (quantity == null || quantity <= 0) continue;

            // Tìm món trong hóa đơn nguồn
            InvoiceDetailEntity sourceDetail = sourceInvoice.getInvoiceDetails().stream()
                    .filter(d -> d.getMenuItem().getId().equals(menuItemId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy món trong hóa đơn nguồn"));

            int remain = sourceDetail.getQuantity() - quantity;
            if (remain > 0) {
                sourceDetail.setQuantity(remain);
                invoiceDetailRepository.save(sourceDetail);
            } else {
                sourceDetail.setDeleted(true);
                invoiceDetailRepository.delete(sourceDetail);
                invoiceDetailRepository.save(sourceDetail);
            }

            // Kiểm tra xem món đó đã tồn tại ở hóa đơn đích chưa
            InvoiceDetailEntity existingDetail = targetInvoice.getInvoiceDetails().stream()
                    .filter(d -> d.getMenuItem().getId().equals(menuItemId))
                    .findFirst()
                    .orElse(null);

            if (existingDetail != null) {
                existingDetail.setQuantity(existingDetail.getQuantity() + quantity);
                invoiceDetailRepository.save(existingDetail);
            } else {
                InvoiceDetailEntity newDetail = new InvoiceDetailEntity(
                        new InvoiceKey(targetInvoice.getId(), menuItemId),
                        targetInvoice,
                        sourceDetail.getMenuItem(),
                        quantity,
                        sourceDetail.getPrice(),
                        false
                );
                invoiceDetailRepository.save(newDetail);
            }
        }

        // Cập nhật trạng thái bàn
        targetTable.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(targetTable);

        recalculateInvoiceTotal(sourceInvoice);
        recalculateInvoiceTotal(targetInvoice);

        // Nếu hóa đơn nguồn không còn món cho bàn thành Available
        List<InvoiceDetailEntity> updatedSourceDetails = invoiceDetailRepository.findByInvoiceId(sourceInvoice.getId());

        if (updatedSourceDetails.isEmpty()) {
            sourceTable.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(sourceTable);
        }
    }





    @Override
    @Transactional
    public void mergeTables(List<Integer> sourceTableIds, Integer targetTableId) {
        if (sourceTableIds == null || sourceTableIds.isEmpty()) {
            throw new RuntimeException("Danh sách bàn cần gộp không được để trống.");
        }

        if (sourceTableIds.contains(targetTableId)) {
            throw new RuntimeException("Không thể gộp chính bàn đích vào bàn đích.");
        }

        TableEntity targetTable = findTableOrThrow(targetTableId);
        List<TableEntity> sourceTables = tableRepository.findAllById(sourceTableIds);

        Map<TableEntity, ReservationEntity> sourceReservations = getActiveReservations(sourceTables);
        Map<TableEntity, InvoiceEntity> sourceInvoices = getSourceInvoices(sourceReservations);

        InvoiceEntity targetInvoice;
        ReservationEntity targetReservation;

        if (targetTable.getStatus() == TableStatus.AVAILABLE) {
            targetInvoice = createPendingInvoice();
            ReservationEntity any = sourceReservations.values().iterator().next();
            targetReservation = new ReservationEntity(
                    new ReservationKey(targetTableId, any.getEmployee().getId(), targetInvoice.getId()),
                    targetTable,
                    any.getEmployee(),
                    targetInvoice,
                    "Khách vãng lai",
                    null,
                    LocalDateTime.now(),
                    false
            );
            reservationRepository.save(targetReservation);
            targetTable.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(targetTable);
        } else {
            targetReservation = findActiveReservationForTable(targetTable);
            targetInvoice = targetReservation.getInvoice();
            if (targetInvoice == null) {
                throw new RuntimeException("Không tìm thấy hóa đơn của bàn đích.");
            }
        }

        for (TableEntity sourceTable : sourceTables) {
            if (sourceTable.getId().equals(targetTableId)) continue;

            InvoiceEntity sourceInvoice = sourceInvoices.get(sourceTable);
            for (InvoiceDetailEntity detail : sourceInvoice.getInvoiceDetails()) {
                InvoiceDetailEntity existing = targetInvoice.getInvoiceDetails().stream()
                        .filter(d -> d.getMenuItem().getId().equals(detail.getMenuItem().getId()))
                        .findFirst()
                        .orElse(null);

                if (existing != null) {
                    existing.setQuantity(existing.getQuantity() + detail.getQuantity());
                    invoiceDetailRepository.save(existing);
                    invoiceDetailRepository.delete(detail);
                } else {
                    InvoiceDetailEntity mergedDetail = new InvoiceDetailEntity(
                            new InvoiceKey(targetInvoice.getId(), detail.getMenuItem().getId()),
                            targetInvoice,
                            detail.getMenuItem(),
                            detail.getQuantity(),
                            detail.getPrice(),
                            false
                    );
                    invoiceDetailRepository.save(mergedDetail);
                }
            }

            ReservationEntity sourceReservation = sourceReservations.get(sourceTable);
            sourceReservation.setDeleted(true);
            reservationRepository.save(sourceReservation);

            sourceInvoice.setStatus(InvoiceStatus.UNDER_REVIEW);
            invoiceRepository.save(sourceInvoice);

            sourceTable.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(sourceTable);
        }

        recalculateInvoiceTotal(targetInvoice);
    }



    private TableEntity findTableOrThrow(Integer id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với id = " + id));
    }

    private ReservationEntity findActiveReservationForTable(TableEntity table) {
        return table.getReservations().stream()
                .filter(r -> !r.getDeleted()
                        && r.getInvoice() != null
                        && (r.getInvoice().getStatus() == InvoiceStatus.PENDING_PAYMENT
                        || r.getInvoice().getStatus() == InvoiceStatus.RESERVED))
                .sorted(Comparator.comparing(ReservationEntity::getReservationDate).reversed())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt bàn chưa thanh toán hoặc đã đặt cho " + table.getTableName()));
    }

    private InvoiceEntity createPendingInvoice() {
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setStatus(InvoiceStatus.PENDING_PAYMENT);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setDeleted(false);
        invoice.setInvoiceDetails(new ArrayList<>());
        return invoiceRepository.save(invoice);
    }


    private void recalculateInvoiceTotal(InvoiceEntity invoice) {
        List<InvoiceDetailEntity> details = invoiceDetailRepository.findByInvoiceId(invoice.getId());
        double total = details.stream()
                .mapToDouble(d -> d.getQuantity() * d.getPrice())
                .sum();
        invoice.setTotalAmount(total);
        invoiceRepository.save(invoice);
    }

    private Map<TableEntity, ReservationEntity> getActiveReservations(List<TableEntity> tables) {
        Map<TableEntity, ReservationEntity> map = new HashMap<>();
        for (TableEntity table : tables) {
            ReservationEntity reservation = table.getReservations().stream()
                    .filter(r -> Boolean.FALSE.equals(r.getDeleted()))
                    .filter(r -> r.getInvoice() != null && (r.getInvoice().getStatus() == InvoiceStatus.PENDING_PAYMENT || r.getInvoice().getStatus() == InvoiceStatus.RESERVED))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đặt bàn đang hoạt động cho bàn " + table.getTableName()));
            map.put(table, reservation);
        }
        return map;
    }

    private Map<TableEntity, InvoiceEntity> getSourceInvoices(Map<TableEntity, ReservationEntity> reservations) {
        Map<TableEntity, InvoiceEntity> map = new HashMap<>();
        for (Map.Entry<TableEntity, ReservationEntity> entry : reservations.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getInvoice());
        }
        return map;
    }

}
