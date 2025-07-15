
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

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
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

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final InvoiceRepository invoiceRepository;
    private final EmployeeRepository employeeRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final MenuItemRepository menuItemRepository;

    /**
     * Lưu đồng bộ reservation, invoice, table khi hủy bàn (xóa mềm)
     */
    @Override
    @Transactional
    public void saveReservationAndRelated(ReservationEntity reservation,
            com.viettridao.cafe.model.InvoiceEntity invoice, com.viettridao.cafe.model.TableEntity table) {
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
        invoice.setStatus(InvoiceStatus.RESERVED); // Đặt trạng thái hóa đơn là RESERVED
        invoice.setCreatedAt(LocalDateTime.now()); // Ghi nhận thời gian tạo hóa đơn
        invoiceRepository.save(invoice); // Lưu hóa đơn vào cơ sở dữ liệu

        // Tạo mới đặt bàn (ReservationEntity)
        ReservationEntity reservation = new ReservationEntity();

        // Tạo khóa chính composite
        ReservationKey reservationKey = new ReservationKey();
        reservationKey.setIdTable(table.getId());
        reservationKey.setIdEmployee(employee.getId());
        reservationKey.setIdInvoice(invoice.getId());

        reservation.setId(reservationKey); // Thiết lập khóa chính
        reservation.setTable(table); // Liên kết bàn với đặt bàn
        reservation.setEmployee(employee); // Liên kết nhân viên với đặt bàn
        reservation.setCustomerName(request.getCustomerName()); // Ghi nhận tên khách hàng
        reservation.setCustomerPhone(request.getCustomerPhone()); // Ghi nhận số điện thoại khách hàng
        reservation.setReservationDate(request.getReservationDate()); // Ghi nhận ngày đặt bàn
        reservation.setInvoice(invoice); // Liên kết hóa đơn với đặt bàn
        reservation.setIsDeleted(false); // Đặt trạng thái không bị xóa

        reservationRepository.save(reservation); // Lưu đặt bàn vào cơ sở dữ liệu

        // Cập nhật trạng thái bàn
        table.setStatus(TableStatus.RESERVED); // Đặt trạng thái bàn là RESERVED
        tableRepository.save(table); // Lưu trạng thái bàn vào cơ sở dữ liệu

        return reservation; // Trả về thực thể đặt bàn vừa được tạo
    }

    /**
     * Gộp nhiều bàn OCCUPIED vào một bàn đích (cộng dồn hóa đơn, cập nhật trạng
     * thái, xóa mềm các bàn nguồn)
     */
    @Override
    @Transactional
    public void mergeTables(MergeTableRequest request, Integer employeeId) {
        // Validate input
        List<Integer> tableIds = request.getTableIds();
        Integer targetTableId = request.getTargetTableId();
        if (tableIds == null || tableIds.size() < 2)
            throw new IllegalArgumentException("Phải chọn ít nhất 2 bàn để gộp");
        if (!tableIds.contains(targetTableId))
            throw new IllegalArgumentException("Bàn gộp đến phải nằm trong danh sách bàn đã chọn");

        // Lấy danh sách bàn OCCUPIED
        List<TableEntity> tables = tableRepository.findAllById(tableIds);
        if (tables.size() != tableIds.size())
            throw new IllegalArgumentException("Có bàn không tồn tại");
        for (TableEntity t : tables) {
            if (t.getStatus() != TableStatus.OCCUPIED)
                throw new IllegalArgumentException("Chỉ được gộp các bàn đang sử dụng (OCCUPIED)");
        }

        // Lấy bàn đích
        TableEntity targetTable = tables.stream().filter(t -> t.getId().equals(targetTableId)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn gộp đến"));

        // Lấy hóa đơn bàn đích
        ReservationEntity targetReservation = reservationRepository.findCurrentReservationByTableId(targetTableId)
                .orElse(null);
        if (targetReservation == null)
            throw new IllegalArgumentException("Không tìm thấy reservation bàn gộp đến");
        InvoiceEntity targetInvoice = targetReservation.getInvoice();
        if (targetInvoice == null)
            throw new IllegalArgumentException("Không tìm thấy hóa đơn bàn gộp đến");

        // Map món đã có trong bàn đích
        Map<Integer, InvoiceDetailEntity> targetDetailMap = new HashMap<>();
        List<InvoiceDetailEntity> targetDetails = invoiceDetailRepository
                .findAllByInvoice_IdAndIsDeletedFalse(targetInvoice.getId());
        for (InvoiceDetailEntity detail : targetDetails) {
            targetDetailMap.put(detail.getMenuItem().getId(), detail);
        }

        // Duyệt các bàn nguồn (trừ bàn đích)
        for (TableEntity srcTable : tables) {
            if (srcTable.getId().equals(targetTableId))
                continue;
            // Lấy reservation và invoice bàn nguồn
            ReservationEntity srcReservation = reservationRepository.findCurrentReservationByTableId(srcTable.getId())
                    .orElse(null);
            if (srcReservation == null)
                continue;
            InvoiceEntity srcInvoice = srcReservation.getInvoice();
            if (srcInvoice == null)
                continue;
            // Đổi trạng thái hóa đơn sang UNDER_REVIEW
            srcInvoice.setStatus(InvoiceStatus.UNDER_REVIEW);
            invoiceRepository.save(srcInvoice);
            // Lấy chi tiết hóa đơn bàn nguồn
            List<InvoiceDetailEntity> srcDetails = invoiceDetailRepository
                    .findAllByInvoice_IdAndIsDeletedFalse(srcInvoice.getId());
            for (InvoiceDetailEntity srcDetail : srcDetails) {
                Integer menuItemId = srcDetail.getMenuItem().getId();
                InvoiceDetailEntity targetDetail = targetDetailMap.get(menuItemId);
                if (targetDetail != null) {
                    // Nếu đã có món này trong bàn đích, tăng số lượng
                    targetDetail.setQuantity(targetDetail.getQuantity() + srcDetail.getQuantity());
                    invoiceDetailRepository.save(targetDetail);
                } else {
                    // Nếu chưa có, clone sang bàn đích
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
                // Đánh dấu chi tiết hóa đơn nguồn là đã xóa
                srcDetail.setIsDeleted(true);
                invoiceDetailRepository.save(srcDetail);
            }
            // Sau khi cộng dồn xong, cập nhật hóa đơn nguồn sang CANCELLED
            srcInvoice.setStatus(InvoiceStatus.CANCELLED);
            srcInvoice.setIsDeleted(true);
            invoiceRepository.save(srcInvoice);
            // Cập nhật reservation nguồn là isDeleted=true
            srcReservation.setIsDeleted(true);
            reservationRepository.save(srcReservation);
            // Cập nhật trạng thái bàn nguồn về AVAILABLE
            srcTable.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(srcTable);
        }
        // Bàn đích giữ nguyên trạng thái OCCUPIED, chỉ cập nhật lại tổng tiền hóa đơn
        // nếu cần
        // (Có thể tính lại tổng tiền ở đây nếu muốn)
    }

    /**
     * Tách bàn: chuyển một phần món từ bàn nguồn sang bàn đích
     */
    @Override
    @Transactional
    public void splitTable(SplitTableRequest request, Integer employeeId) {
        Integer sourceTableId = request.getSourceTableId();
        Integer targetTableId = request.getTargetTableId();

        // Validate basic input
        if (sourceTableId.equals(targetTableId)) {
            throw new IllegalArgumentException("Bàn nguồn và bàn đích không được trùng nhau");
        }

        // Lấy thông tin bàn nguồn và bàn đích
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

        // Validate trạng thái bàn
        if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalArgumentException("Chỉ có thể tách từ bàn đang sử dụng (OCCUPIED)");
        }
        if (targetTable.getStatus() != TableStatus.AVAILABLE && targetTable.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalArgumentException("Bàn đích phải là bàn trống (AVAILABLE) hoặc đang sử dụng (OCCUPIED)");
        }

        // Lấy reservation và invoice của bàn nguồn
        ReservationEntity sourceReservation = reservationRepository.findCurrentReservationByTableId(sourceTableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy reservation của bàn nguồn"));
        InvoiceEntity sourceInvoice = sourceReservation.getInvoice();
        if (sourceInvoice == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn của bàn nguồn");
        }

        // Đổi trạng thái hóa đơn nguồn sang UNDER_REVIEW
        sourceInvoice.setStatus(InvoiceStatus.UNDER_REVIEW);
        invoiceRepository.save(sourceInvoice);

        // Lấy chi tiết hóa đơn hiện tại của bàn nguồn
        List<InvoiceDetailEntity> sourceDetails = invoiceDetailRepository
                .findAllByInvoice_IdAndIsDeletedFalse(sourceInvoice.getId());
        Map<Integer, InvoiceDetailEntity> sourceDetailMap = new HashMap<>();
        for (InvoiceDetailEntity detail : sourceDetails) {
            sourceDetailMap.put(detail.getMenuItem().getId(), detail);
        }

        // Validate số lượng món cần tách
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

        // Xử lý bàn đích
        InvoiceEntity targetInvoice;
        ReservationEntity targetReservation;
        Map<Integer, InvoiceDetailEntity> targetDetailMap = new HashMap<>();

        if (targetTable.getStatus() == TableStatus.AVAILABLE) {
            // Bàn đích trống -> tạo mới invoice, reservation
            EmployeeEntity employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên"));

            // Tạo hóa đơn mới
            targetInvoice = new InvoiceEntity();
            targetInvoice.setTotalAmount(0.0);
            targetInvoice.setCreatedAt(LocalDateTime.now());
            targetInvoice.setStatus(InvoiceStatus.PENDING_PAYMENT);
            targetInvoice.setIsDeleted(false);
            targetInvoice = invoiceRepository.save(targetInvoice);

            // Tạo reservation mới
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

            // Cập nhật trạng thái bàn đích sang OCCUPIED
            targetTable.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(targetTable);

        } else {
            // Bàn đích đang sử dụng -> lấy invoice hiện tại
            targetReservation = reservationRepository.findCurrentReservationByTableId(targetTableId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy reservation của bàn đích"));
            targetInvoice = targetReservation.getInvoice();
            if (targetInvoice == null) {
                throw new IllegalArgumentException("Không tìm thấy hóa đơn của bàn đích");
            }

            // Lấy chi tiết hóa đơn hiện tại của bàn đích
            List<InvoiceDetailEntity> targetDetails = invoiceDetailRepository
                    .findAllByInvoice_IdAndIsDeletedFalse(targetInvoice.getId());
            for (InvoiceDetailEntity detail : targetDetails) {
                targetDetailMap.put(detail.getMenuItem().getId(), detail);
            }
        }

        // Chuyển món từ bàn nguồn sang bàn đích
        for (SplitTableRequest.SplitItemRequest item : request.getItems()) {
            Integer menuItemId = item.getMenuItemId();
            Integer splitQuantity = item.getQuantity();

            InvoiceDetailEntity sourceDetail = sourceDetailMap.get(menuItemId);
            MenuItemEntity menuItem = sourceDetail.getMenuItem();

            // Xử lý bàn đích
            InvoiceDetailEntity targetDetail = targetDetailMap.get(menuItemId);
            if (targetDetail != null) {
                // Món đã có trong bàn đích -> cộng số lượng
                targetDetail.setQuantity(targetDetail.getQuantity() + splitQuantity);
                invoiceDetailRepository.save(targetDetail);
            } else {
                // Món chưa có trong bàn đích -> tạo mới
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

            // Xử lý bàn nguồn
            int remainingQuantity = sourceDetail.getQuantity() - splitQuantity;
            if (remainingQuantity > 0) {
                // Còn lại một phần -> cập nhật số lượng
                sourceDetail.setQuantity(remainingQuantity);
                invoiceDetailRepository.save(sourceDetail);
            } else {
                // Tách hết -> đánh dấu xóa mềm
                sourceDetail.setIsDeleted(true);
                invoiceDetailRepository.save(sourceDetail);
            }
        }

        // Kiểm tra xem bàn nguồn còn món nào không
        List<InvoiceDetailEntity> remainingSourceDetails = invoiceDetailRepository
                .findAllByInvoice_IdAndIsDeletedFalse(sourceInvoice.getId());

        if (remainingSourceDetails.isEmpty()) {
            // Không còn món nào -> hủy hóa đơn và reservation, chuyển bàn về AVAILABLE
            sourceInvoice.setStatus(InvoiceStatus.CANCELLED);
            sourceInvoice.setIsDeleted(true);
            invoiceRepository.save(sourceInvoice);

            sourceReservation.setIsDeleted(true);
            reservationRepository.save(sourceReservation);

            sourceTable.setStatus(TableStatus.AVAILABLE);
            tableRepository.save(sourceTable);
        } else {
            // Còn món -> chuyển trạng thái hóa đơn về PENDING_PAYMENT
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

        // Lấy thông tin bàn nguồn và bàn đích
        TableEntity sourceTable = tableRepository.findById(sourceTableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn nguồn"));
        TableEntity targetTable = tableRepository.findById(targetTableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn đích"));

        // Chỉ cho phép chuyển từ OCCUPIED sang AVAILABLE
        if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
            throw new IllegalArgumentException("Chỉ có thể chuyển từ bàn đang sử dụng (OCCUPIED)");
        }
        if (targetTable.getStatus() != TableStatus.AVAILABLE) {
            throw new IllegalArgumentException("Chỉ có thể chuyển sang bàn trống (AVAILABLE)");
        }

        // Lấy reservation và invoice của bàn nguồn
        ReservationEntity sourceReservation = reservationRepository.findCurrentReservationByTableId(sourceTableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy reservation của bàn nguồn"));
        InvoiceEntity sourceInvoice = sourceReservation.getInvoice();
        if (sourceInvoice == null) {
            throw new IllegalArgumentException("Không tìm thấy hóa đơn của bàn nguồn");
        }

        // Lấy nhân viên thực hiện chuyển bàn
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên thực hiện chuyển bàn"));

        // Tạo reservation mới cho bàn đích (copy thông tin khách, ngày giờ, hóa đơn)
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

        // Cập nhật lại invoice nếu cần (có thể giữ nguyên, chỉ cập nhật trạng thái)
        sourceInvoice.setIsDeleted(false); // Đảm bảo không bị xóa mềm
        invoiceRepository.save(sourceInvoice);

        // Cập nhật trạng thái bàn đích thành OCCUPIED
        targetTable.setStatus(TableStatus.OCCUPIED);
        tableRepository.save(targetTable);

        // Cập nhật trạng thái bàn nguồn thành AVAILABLE
        sourceTable.setStatus(TableStatus.AVAILABLE);
        tableRepository.save(sourceTable);

        // Xóa mềm reservation ở bàn nguồn
        sourceReservation.setIsDeleted(true);
        reservationRepository.save(sourceReservation);

        // Không cần xóa mềm invoice hay invoice detail vì đã chuyển toàn bộ sang bàn
        // mới
        // (invoice detail chỉ liên kết với invoice, không cần thay đổi)
    }
}
