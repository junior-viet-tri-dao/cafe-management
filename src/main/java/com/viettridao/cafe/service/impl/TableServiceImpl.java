package com.viettridao.cafe.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.table.SwitchTableRequest;
import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.mapper.TableMapper;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.InvoiceKey;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {

        private final TableRepository tableRepository;
        private final TableMapper tableMapper;
        private final ReservationRepository reservationRepository;
        private final InvoiceRepository invoiceRepository;
        private final InvoiceDetailRepository invoiceDetailRepository;

        @Override
        @Transactional
        public void splitTable(Integer sourceTableId, Integer targetTableId,
                        Map<Integer, Integer> menuItemIdToQuantity) {
                TableEntity sourceTable = getTableOrThrow(sourceTableId);
                TableEntity targetTable = getTableOrThrow(targetTableId);

                // 1. Lấy reservation và invoice của bàn nguồn
                ReservationEntity sourceReservation = getActiveReservation(sourceTable);
                InvoiceEntity sourceInvoice = sourceReservation.getInvoice();

                // 2. Tạo hóa đơn và reservation mới cho bàn đích
                InvoiceEntity targetInvoice = createNewInvoice();
                ReservationEntity targetReservation = new ReservationEntity();
                ReservationKey targetKey = new ReservationKey(
                                targetTable.getId(),
                                sourceReservation.getEmployee().getId(),
                                targetInvoice.getId());
                targetReservation.setId(targetKey);
                targetReservation.setTable(targetTable);
                targetReservation.setEmployee(sourceReservation.getEmployee());
                targetReservation.setInvoice(targetInvoice);
                targetReservation.setCustomerName(null);
                targetReservation.setCustomerPhone(null);
                targetReservation.setReservationDate(java.time.LocalDateTime.now());
                targetReservation.setIsDeleted(false);
                reservationRepository.save(targetReservation);

                // 3. Duyệt từng món cần tách
                for (Map.Entry<Integer, Integer> entry : menuItemIdToQuantity.entrySet()) {
                        Integer menuItemId = entry.getKey();
                        Integer splitQuantity = entry.getValue();
                        if (splitQuantity == null || splitQuantity <= 0)
                                continue;

                        // Tìm detail ở bàn nguồn
                        InvoiceDetailEntity sourceDetail = sourceInvoice.getInvoiceDetails().stream()
                                        .filter(d -> d.getMenuItem().getId().equals(menuItemId))
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Không tìm thấy món trong hóa đơn nguồn"));

                        // Trừ số lượng ở bàn nguồn
                        int remain = sourceDetail.getQuantity() - splitQuantity;
                        if (remain > 0) {
                                sourceDetail.setQuantity(remain);
                                invoiceDetailRepository.save(sourceDetail);
                        } else {
                                invoiceDetailRepository.delete(sourceDetail);
                        }

                        // Thêm vào hóa đơn bàn đích
                        InvoiceDetailEntity targetDetail = new InvoiceDetailEntity();
                        targetDetail.setId(new InvoiceKey(targetInvoice.getId(), menuItemId));
                        targetDetail.setInvoice(targetInvoice);
                        targetDetail.setMenuItem(sourceDetail.getMenuItem());
                        targetDetail.setQuantity(splitQuantity);
                        targetDetail.setPrice(sourceDetail.getPrice());
                        targetDetail.setIsDeleted(false);
                        invoiceDetailRepository.save(targetDetail);
                }

                // 4. Cập nhật trạng thái bàn đích
                updateTableStatus(targetTable, TableStatus.OCCUPIED);

                // 4.1. Cập nhật lại tổng tiền cho hóa đơn mới (bàn đích)
                List<InvoiceDetailEntity> targetDetails = invoiceDetailRepository
                                .findByInvoiceId(targetInvoice.getId());
                BigDecimal targetTotal = BigDecimal.ZERO;
                for (InvoiceDetailEntity detail : targetDetails) {
                        targetTotal = targetTotal.add(
                                        BigDecimal.valueOf(detail.getQuantity()).multiply(detail.getPrice()));
                }
                targetInvoice.setTotalAmount(targetTotal);
                invoiceRepository.save(targetInvoice);

                // 4.2. Cập nhật lại tổng tiền cho hóa đơn nguồn (bàn nguồn)
                BigDecimal sourceTotal = BigDecimal.ZERO;
                for (InvoiceDetailEntity detail : sourceInvoice.getInvoiceDetails()) {
                        sourceTotal = sourceTotal.add(
                                        BigDecimal.valueOf(detail.getQuantity()).multiply(detail.getPrice()));
                }
                sourceInvoice.setTotalAmount(sourceTotal);
                invoiceRepository.save(sourceInvoice);

                // 5. Nếu bàn nguồn hết món thì cập nhật trạng thái AVAILABLE
                if (sourceInvoice.getInvoiceDetails().isEmpty()) {
                        updateTableStatus(sourceTable, TableStatus.AVAILABLE);
                }
        }

        @Override
        @Transactional
        public void mergeTables(List<Integer> mergeTableIds, Integer targetTableId) {
                TableEntity targetTable = getTableOrThrow(targetTableId);
                List<TableEntity> sourceTables = tableRepository.findAllById(mergeTableIds);

                Map<TableEntity, ReservationEntity> sourceReservations = getSourceReservations(sourceTables);
                Map<TableEntity, InvoiceEntity> sourceInvoices = getSourceInvoices(sourceReservations);

                boolean isTargetAvailable = targetTable.getStatus() == TableStatus.AVAILABLE;
                InvoiceEntity targetInvoice;
                ReservationEntity targetReservation;

                if (isTargetAvailable) {
                        targetInvoice = createNewInvoice();
                        targetReservation = createNewReservation(targetTable, targetInvoice, sourceReservations);
                        updateTableStatus(targetTable, TableStatus.OCCUPIED);
                        mergeAllInvoiceDetailsToNewInvoice(sourceTables, sourceInvoices, targetInvoice);
                } else {
                        targetReservation = sourceReservations.get(targetTable);
                        targetInvoice = sourceInvoices.get(targetTable);
                        if (targetInvoice == null)
                                throw new RuntimeException("Không tìm thấy hóa đơn bàn đích!");
                }

                mergeSourceInvoicesToTarget(sourceTables, targetTableId, targetInvoice, sourceInvoices,
                                sourceReservations,
                                isTargetAvailable);

                BigDecimal totalAmount = calculateTotalAmount(sourceTables, sourceInvoices, isTargetAvailable,
                                targetTableId,
                                targetInvoice);
                targetInvoice.setTotalAmount(totalAmount);
                invoiceRepository.save(targetInvoice);
        }

        private TableEntity getTableOrThrow(Integer tableId) {
                return tableRepository.findById(tableId)
                                .orElseThrow(() -> new RuntimeException("Bàn không tồn tại: " + tableId));
        }

        private Map<TableEntity, ReservationEntity> getSourceReservations(List<TableEntity> tables) {
                Map<TableEntity, ReservationEntity> map = new HashMap<>();
                for (TableEntity table : tables) {
                        ReservationEntity reservation = table.getReservations().stream()
                                        .filter(r -> Boolean.FALSE.equals(r.getIsDeleted()))
                                        .filter(r -> r.getInvoice() != null
                                                        && r.getInvoice().getStatus() == InvoiceStatus.UNPAID)
                                        .findFirst()
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Không tìm thấy đặt bàn chưa thanh toán cho bàn "
                                                                        + table.getTableName()));
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

        private InvoiceEntity createNewInvoice() {
                InvoiceEntity invoice = new InvoiceEntity();
                invoice.setStatus(InvoiceStatus.UNPAID);
                invoice.setCreatedAt(LocalDateTime.now());
                invoice.setIsDeleted(false);
                invoice.setInvoiceDetails(new ArrayList<>());
                return invoiceRepository.save(invoice);
        }

        private ReservationEntity createNewReservation(
                        TableEntity targetTable,
                        InvoiceEntity targetInvoice,
                        Map<TableEntity, ReservationEntity> sourceReservations) {
                ReservationEntity anySourceReservation = sourceReservations.values().iterator().next();
                ReservationEntity newReservation = new ReservationEntity();
                ReservationKey newKey = new ReservationKey(
                                targetTable.getId(),
                                anySourceReservation.getEmployee().getId(),
                                targetInvoice.getId());
                newReservation.setId(newKey);
                newReservation.setTable(targetTable);
                newReservation.setEmployee(anySourceReservation.getEmployee());
                newReservation.setInvoice(targetInvoice);
                newReservation.setCustomerName(null);
                newReservation.setCustomerPhone(null);
                newReservation.setReservationDate(LocalDateTime.now());
                newReservation.setIsDeleted(false);
                return reservationRepository.save(newReservation);
        }

        private void updateTableStatus(TableEntity table, TableStatus status) {
                table.setStatus(status);
                tableRepository.save(table);
        }

        private void mergeAllInvoiceDetailsToNewInvoice(
                        List<TableEntity> sourceTables,
                        Map<TableEntity, InvoiceEntity> sourceInvoices,
                        InvoiceEntity targetInvoice) {
                Map<Integer, Integer> menuItemIdToQuantity = new HashMap<>();
                Map<Integer, InvoiceDetailEntity> menuItemIdToDetail = new HashMap<>();

                for (TableEntity source : sourceTables) {
                        InvoiceEntity sourceInvoice = sourceInvoices.get(source);
                        List<InvoiceDetailEntity> sourceDetails = sourceInvoice.getInvoiceDetails();
                        if (sourceDetails == null)
                                sourceDetails = new ArrayList<>();
                        for (InvoiceDetailEntity detail : sourceDetails) {
                                int menuItemId = detail.getMenuItem().getId();
                                menuItemIdToQuantity.put(menuItemId,
                                                menuItemIdToQuantity.getOrDefault(menuItemId, 0)
                                                                + detail.getQuantity());
                                if (!menuItemIdToDetail.containsKey(menuItemId)) {
                                        InvoiceDetailEntity temp = new InvoiceDetailEntity();
                                        temp.setMenuItem(detail.getMenuItem());
                                        temp.setPrice(detail.getPrice());
                                        menuItemIdToDetail.put(menuItemId, temp);
                                }
                                invoiceDetailRepository.delete(detail);
                        }
                }

                for (Map.Entry<Integer, Integer> entry : menuItemIdToQuantity.entrySet()) {
                        Integer menuItemId = entry.getKey();
                        Integer totalQuantity = entry.getValue();
                        InvoiceDetailEntity sample = menuItemIdToDetail.get(menuItemId);

                        InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                        newDetail.setId(new InvoiceKey(targetInvoice.getId(), menuItemId));
                        newDetail.setInvoice(targetInvoice);
                        newDetail.setMenuItem(sample.getMenuItem());
                        newDetail.setQuantity(totalQuantity);
                        newDetail.setPrice(sample.getPrice());
                        newDetail.setIsDeleted(false);
                        invoiceDetailRepository.save(newDetail);
                }
        }

        private void mergeSourceInvoicesToTarget(
                        List<TableEntity> sourceTables,
                        Integer targetTableId,
                        InvoiceEntity targetInvoice,
                        Map<TableEntity, InvoiceEntity> sourceInvoices,
                        Map<TableEntity, ReservationEntity> sourceReservations,
                        boolean isTargetAvailable) {
                for (TableEntity source : sourceTables) {
                        if (source.getId().equals(targetTableId))
                                continue; // bỏ qua bàn đích nếu là bàn nguồn

                        InvoiceEntity sourceInvoice = sourceInvoices.get(source);
                        List<InvoiceDetailEntity> sourceDetails = sourceInvoice.getInvoiceDetails();
                        if (sourceDetails == null)
                                sourceDetails = new ArrayList<>();

                        List<InvoiceDetailEntity> targetDetails = targetInvoice.getInvoiceDetails();
                        if (targetDetails == null)
                                targetDetails = new ArrayList<>();

                        List<InvoiceDetailEntity> detailsToRemove = new ArrayList<>();
                        for (InvoiceDetailEntity detail : sourceDetails) {
                                // Kiểm tra trùng món
                                InvoiceDetailEntity existed = targetDetails.stream()
                                                .filter(d -> d.getMenuItem().getId()
                                                                .equals(detail.getMenuItem().getId()))
                                                .findFirst()
                                                .orElse(null);

                                if (existed != null) {
                                        existed.setQuantity(existed.getQuantity() + detail.getQuantity());
                                        invoiceDetailRepository.save(existed);
                                        detailsToRemove.add(detail);
                                } else {
                                        InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                                        newDetail.setId(new InvoiceKey(targetInvoice.getId(),
                                                        detail.getMenuItem().getId()));
                                        newDetail.setInvoice(targetInvoice);
                                        newDetail.setMenuItem(detail.getMenuItem());
                                        newDetail.setQuantity(detail.getQuantity());
                                        newDetail.setPrice(detail.getPrice());
                                        newDetail.setIsDeleted(false);
                                        invoiceDetailRepository.save(newDetail);
                                        targetDetails.add(newDetail); // <--- THÊM DÒNG NÀY
                                        detailsToRemove.add(detail);
                                }
                        }
                        // Xóa các InvoiceDetail cũ đã cộng dồn
                        for (InvoiceDetailEntity detail : detailsToRemove) {
                                invoiceDetailRepository.delete(detail);
                        }
                        // --------------------
                        // Xóa reservation cũ
                        ReservationEntity sourceReservation = sourceReservations.get(source);
                        reservationRepository.delete(sourceReservation);

                        // Đổi trạng thái hóa đơn cũ thành MERGE_TABLE (bạn cần định nghĩa trạng thái
                        // này trong InvoiceStatus)
                        sourceInvoice.setStatus(InvoiceStatus.MERGE_TABLE);
                        sourceInvoice.setIsDeleted(false); // vẫn giữ lại hóa đơn
                        invoiceRepository.save(sourceInvoice);

                        // Cập nhật trạng thái bàn nguồn thành AVAILABLE
                        updateTableStatus(source, TableStatus.AVAILABLE);
                }
        }

        private BigDecimal calculateTotalAmount(
                        List<TableEntity> sourceTables,
                        Map<TableEntity, InvoiceEntity> sourceInvoices,
                        boolean isTargetAvailable,
                        Integer targetTableId,
                        InvoiceEntity targetInvoice) {
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (TableEntity source : sourceTables) {
                        InvoiceEntity sourceInvoice = sourceInvoices.get(source);
                        // Nếu là bàn đích và không phải bàn trống thì bỏ qua (vì hóa đơn đích sẽ giữ
                        // lại)
                        if (!isTargetAvailable && source.getId().equals(targetTableId))
                                continue;
                        if (sourceInvoice != null && sourceInvoice.getTotalAmount() != null) {
                                totalAmount = totalAmount.add(sourceInvoice.getTotalAmount());
                        }
                }
                // Nếu bàn đích là một trong các bàn nguồn, cộng thêm tổng tiền hóa đơn đích cũ
                if (!isTargetAvailable && targetInvoice.getTotalAmount() != null) {
                        totalAmount = totalAmount.add(targetInvoice.getTotalAmount());
                }
                return totalAmount;
        }

        @Override
        @Transactional
        public TableResponse switchTable(SwitchTableRequest request) {
                Integer fromTableId = request.getFromTableId();
                Integer toTableId = request.getToTableId();

                if (fromTableId.equals(toTableId)) {
                        throw new RuntimeException("Không thể chuyển sang chính bàn này!");
                }

                TableEntity fromTable = tableRepository.findById(fromTableId)
                                .orElseThrow(() -> new RuntimeException("Bàn không tồn tại: " + fromTableId));
                TableEntity toTable = tableRepository.findById(toTableId)
                                .orElseThrow(() -> new RuntimeException("Bàn không tồn tại: " + toTableId));

                if (toTable.getStatus() != TableStatus.AVAILABLE) {
                        throw new RuntimeException("Bàn chuyển đến phải đang trống!");
                }

                ReservationEntity fromReservation = fromTable.getReservations().stream()
                                .filter(r -> Boolean.FALSE.equals(r.getIsDeleted()))
                                .filter(r -> r.getInvoice() != null
                                                && r.getInvoice().getStatus() == InvoiceStatus.UNPAID)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException(
                                                "Không tìm thấy đặt bàn chưa thanh toán cho bàn này!"));

                ReservationKey fromReservationId = fromReservation.getId();
                TableStatus fromTableStatusBeforeSwitch = fromTable.getStatus();

                // Tạo reservation mới trước, nếu thành công mới xóa reservation cũ
                ReservationEntity newReservation = new ReservationEntity();
                newReservation.setId(new ReservationKey(
                                toTableId,
                                fromReservationId.getIdEmployee(),
                                fromReservationId.getIdInvoice()));
                newReservation.setTable(toTable);
                newReservation.setEmployee(fromReservation.getEmployee());
                newReservation.setInvoice(fromReservation.getInvoice());
                newReservation.setCustomerName(fromReservation.getCustomerName());
                newReservation.setCustomerPhone(fromReservation.getCustomerPhone());
                newReservation.setReservationDate(fromReservation.getReservationDate());
                newReservation.setIsDeleted(false);
                reservationRepository.save(newReservation);

                // Xóa reservation cũ
                reservationRepository.delete(fromReservation);

                // Cập nhật trạng thái bàn
                fromTable.setStatus(TableStatus.AVAILABLE);
                toTable.setStatus(fromTableStatusBeforeSwitch);

                tableRepository.save(fromTable);
                TableEntity updatedToTable = tableRepository.save(toTable);

                return tableMapper.convertToDto(updatedToTable);

        }

        @Override
        public List<TableResponse> getTableByStatus(TableStatus status) {
                return tableRepository.findByStatus(status).stream()
                                .map(tableMapper::convertToDto)
                                .toList();
        }

        @Override
        public List<TableResponse> getAllTables() {
                return tableRepository.findAllByIsDeletedFalse()
                                .stream()
                                .map(tableMapper::convertToDto)
                                .toList();
        }

        @Override
        public TableEntity getTableById(Integer tableId) {
                return tableRepository.findById(tableId)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với ID: " + tableId));
        }

        private ReservationEntity getActiveReservation(TableEntity table) {
                return table.getReservations().stream()
                                .filter(r -> Boolean.FALSE.equals(r.getIsDeleted()))
                                .filter(r -> r.getInvoice() != null
                                                && r.getInvoice().getStatus() == InvoiceStatus.UNPAID)
                                .findFirst()
                                .orElseThrow(() -> new RuntimeException(
                                                "Không tìm thấy đặt bàn chưa thanh toán cho bàn "
                                                                + table.getTableName()));
        }

}
