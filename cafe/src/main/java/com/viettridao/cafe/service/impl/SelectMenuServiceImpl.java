package com.viettridao.cafe.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.sales.CreateSelectMenuRequest;
import com.viettridao.cafe.dto.response.sales.MenuItemResponse;
import com.viettridao.cafe.dto.response.sales.OrderDetailRessponse;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.mapper.OrderDetailMapper;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.InvoiceKey;
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
import com.viettridao.cafe.service.SelectMenuService;

import lombok.RequiredArgsConstructor;

/**
 * Triển khai logic chọn món và tạo order cho bàn.
 */
@Service
@RequiredArgsConstructor
public class SelectMenuServiceImpl implements SelectMenuService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceDetailRepository invoiceDetailRepository;
    private final MenuItemRepository menuItemRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderDetailRessponse createOrderForAvailableTable(
            CreateSelectMenuRequest request, Integer employeeId) {
        // 1. Kiểm tra trạng thái bàn
        TableEntity table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new IllegalArgumentException("Bàn không tồn tại!"));

        // 2. Lấy thông tin nhân viên từ DB (theo employeeId lấy từ session)
        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Nhân viên không tồn tại!"));

        InvoiceEntity invoice = null;
        ReservationEntity reservation = null;

        if (table.getStatus() == TableStatus.AVAILABLE) {
            // Bàn trống: tạo mới hóa đơn, reservation, chi tiết hóa đơn
            invoice = new InvoiceEntity();
            invoice.setStatus(InvoiceStatus.PENDING_PAYMENT);
            invoice.setCreatedAt(LocalDateTime.now());
            invoiceRepository.save(invoice);

            ReservationKey reservationKey = new ReservationKey();
            reservationKey.setIdTable(table.getId());
            reservationKey.setIdEmployee(employee.getId());
            reservationKey.setIdInvoice(invoice.getId());

            reservation = new ReservationEntity();
            reservation.setId(reservationKey);
            reservation.setTable(table);
            reservation.setEmployee(employee);
            reservation.setInvoice(invoice);
            reservation.setCustomerName(request.getCustomerName());
            reservation.setCustomerPhone(request.getCustomerPhone());
            reservation.setReservationDate(LocalDateTime.now());
            reservation.setIsDeleted(false);
            reservationRepository.save(reservation);

            // Lưu chi tiết hóa đơn
            for (CreateSelectMenuRequest.MenuOrderItem item : request.getItems()) {
                MenuItemEntity menuItem = menuItemRepository.findById(item.getMenuItemId())
                        .orElseThrow(() -> new IllegalArgumentException("Món ăn không tồn tại!"));
                InvoiceKey invoiceKey = new InvoiceKey();
                invoiceKey.setIdInvoice(invoice.getId());
                invoiceKey.setIdMenuItem(menuItem.getId());

                InvoiceDetailEntity detail = new InvoiceDetailEntity();
                detail.setId(invoiceKey);
                detail.setInvoice(invoice);
                detail.setMenuItem(menuItem);
                detail.setQuantity(item.getQuantity());
                detail.setPrice(menuItem.getCurrentPrice());
                detail.setIsDeleted(false);
                invoiceDetailRepository.save(detail);
            }

            // Cập nhật trạng thái bàn
            table.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(table);
        } else if (table.getStatus() == TableStatus.RESERVED) {
            // Bàn đã đặt: lấy reservation, hóa đơn hiện tại, chuyển trạng thái hóa đơn,
            // bàn, thêm/cập nhật món
            reservation = reservationRepository.findAll().stream()
                    .filter(r -> r.getTable().getId().equals(table.getId()) && !Boolean.TRUE.equals(r.getIsDeleted()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy reservation cho bàn đã đặt!"));
            invoice = reservation.getInvoice();
            invoice.setStatus(InvoiceStatus.PENDING_PAYMENT);
            invoiceRepository.save(invoice);
            table.setStatus(TableStatus.OCCUPIED);
            tableRepository.save(table);
            // Thêm/cập nhật món
            updateOrAddInvoiceDetails(invoice, request);
        } else if (table.getStatus() == TableStatus.OCCUPIED) {
            // Bàn đang sử dụng: lấy reservation, hóa đơn hiện tại, thêm/cập nhật món
            reservation = reservationRepository.findAll().stream()
                    .filter(r -> r.getTable().getId().equals(table.getId()) && !Boolean.TRUE.equals(r.getIsDeleted()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Không tìm thấy reservation cho bàn đang sử dụng!"));
            invoice = reservation.getInvoice();
            // Thêm/cập nhật món
            updateOrAddInvoiceDetails(invoice, request);
        } else {
            throw new IllegalStateException("Trạng thái bàn không hợp lệ!");
        }

        // Tính lại total amount cho invoice
        updateInvoiceTotalAmount(invoice);

        // Lấy lại danh sách chi tiết hóa đơn
        List<InvoiceDetailEntity> invoiceDetails = invoiceDetailRepository
                .findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());
        // Trả về response
        return orderDetailMapper.toOrderDetailResponse(table, invoice, reservation, invoiceDetails);
    }

    /**
     * Thêm mới hoặc cập nhật số lượng món trong chi tiết hóa đơn
     */
    private void updateOrAddInvoiceDetails(InvoiceEntity invoice, CreateSelectMenuRequest request) {
        for (CreateSelectMenuRequest.MenuOrderItem item : request.getItems()) {
            MenuItemEntity menuItem = menuItemRepository.findById(item.getMenuItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Món ăn không tồn tại!"));
            InvoiceKey invoiceKey = new InvoiceKey();
            invoiceKey.setIdInvoice(invoice.getId());
            invoiceKey.setIdMenuItem(menuItem.getId());

            InvoiceDetailEntity detail = invoiceDetailRepository.findById(invoiceKey).orElse(null);
            if (detail != null && !Boolean.TRUE.equals(detail.getIsDeleted())) {
                // Nếu đã có thì cộng dồn số lượng
                detail.setQuantity(detail.getQuantity() + item.getQuantity());
                invoiceDetailRepository.save(detail);
            } else {
                // Nếu chưa có thì thêm mới
                InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
                newDetail.setId(invoiceKey);
                newDetail.setInvoice(invoice);
                newDetail.setMenuItem(menuItem);
                newDetail.setQuantity(item.getQuantity());
                newDetail.setPrice(menuItem.getCurrentPrice());
                newDetail.setIsDeleted(false);
                invoiceDetailRepository.save(newDetail);
            }
        }
    }

    /**
     * Tính và cập nhật tổng tiền hóa đơn
     */
    private void updateInvoiceTotalAmount(InvoiceEntity invoice) {
        List<InvoiceDetailEntity> details = invoiceDetailRepository
                .findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());

        double totalAmount = details.stream()
                .mapToDouble(detail -> detail.getPrice() * detail.getQuantity())
                .sum();

        invoice.setTotalAmount(totalAmount);
        invoiceRepository.save(invoice);
    }

    @Override
    public List<MenuItemResponse> getMenuItems() {
        List<MenuItemEntity> menuEntities = menuItemRepository.findByIsDeletedFalseOrIsDeletedIsNull();
        return MenuItemMapper.toMenuItemResponseList(menuEntities);
    }

}
