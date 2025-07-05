package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.tables.MenuItemSplitRequest;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.InvoiceKey;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.InvoiceDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableSplitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableSplitServiceImpl implements TableSplitService {

	private final TableRepository tableRepository;
	private final InvoiceRepository invoiceRepository;
	private final InvoiceDetailRepository invoiceDetailRepository;
	private final ReservationRepository reservationRepository;
	private final EmployeeRepository employeeRepository;

	@Transactional
	@Override
	public void splitTable(Integer fromTableId, Integer toTableId, List<MenuItemSplitRequest> itemsToSplit,
			String customerName, String customerPhone) {

		TableEntity fromTable = tableRepository.findById(fromTableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn"));
		TableEntity toTable = tableRepository.findById(toTableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn đích"));

		if (toTable.getStatus() != TableStatus.AVAILABLE) {
			throw new RuntimeException("Không thể tách vào bàn đang phục vụ hoặc đã được đặt trước");
		}

		InvoiceEntity fromInvoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(fromTableId,
						InvoiceStatus.UNPAID);
		if (fromInvoice == null || fromInvoice.getInvoiceDetails() == null) {
			throw new RuntimeException("Bàn nguồn không có hóa đơn hoặc hóa đơn không có món ăn");
		}

		InvoiceEntity toInvoice = new InvoiceEntity();
		toInvoice.setStatus(InvoiceStatus.UNPAID);
		toInvoice.setCreatedAt(LocalDateTime.now());
		toInvoice.setTotalAmount(0.0);
		toInvoice.setIsDeleted(false);
		toInvoice = invoiceRepository.save(toInvoice);

		reservationRepository.save(createReservation(toInvoice, toTable, customerName, customerPhone));

		toTable.setStatus(TableStatus.OCCUPIED);
		tableRepository.save(toTable);

		for (MenuItemSplitRequest item : itemsToSplit) {
			moveMenuItem(fromInvoice, toInvoice, item);
		}

		recalculateTotal(fromInvoice);
		recalculateTotal(toInvoice);
	}

	private void moveMenuItem(InvoiceEntity fromInvoice, InvoiceEntity toInvoice, MenuItemSplitRequest item) {
		Optional<InvoiceDetailEntity> fromDetailOpt = fromInvoice.getInvoiceDetails().stream().filter(
				d -> !Boolean.TRUE.equals(d.getIsDeleted()) && d.getMenuItem().getId().equals(item.getMenuItemId()))
				.findFirst();

		if (fromDetailOpt.isEmpty())
			return;

		InvoiceDetailEntity fromDetail = fromDetailOpt.get();
		int availableQty = fromDetail.getQuantity();
		int moveQty = item.getQuantityToMove();

		if (moveQty > availableQty) {
			throw new RuntimeException("Số lượng tách vượt quá số lượng hiện tại");
		}

		fromDetail.setQuantity(availableQty - moveQty);
		invoiceDetailRepository.save(fromDetail);

		Optional<InvoiceDetailEntity> toDetailOpt = invoiceDetailRepository
				.findById(new InvoiceKey(toInvoice.getId(), item.getMenuItemId()));

		if (toDetailOpt.isPresent()) {
			InvoiceDetailEntity toDetail = toDetailOpt.get();
			toDetail.setQuantity(toDetail.getQuantity() + moveQty);
			invoiceDetailRepository.save(toDetail);
		} else {
			InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
			InvoiceKey key = new InvoiceKey();
			key.setIdInvoice(toInvoice.getId());
			key.setIdMenuItem(fromDetail.getMenuItem().getId());

			newDetail.setId(key);
			newDetail.setInvoice(toInvoice);
			newDetail.setMenuItem(fromDetail.getMenuItem());
			newDetail.setQuantity(moveQty);
			newDetail.setPrice(fromDetail.getPrice());
			newDetail.setIsDeleted(false);

			invoiceDetailRepository.save(newDetail);
		}
	}

	private void recalculateTotal(InvoiceEntity invoice) {
		double total = invoiceDetailRepository.findByInvoice_Id(invoice.getId()).stream()
				.filter(d -> !Boolean.TRUE.equals(d.getIsDeleted())).mapToDouble(d -> d.getPrice() * d.getQuantity())
				.sum();
		invoice.setTotalAmount(total);
		invoiceRepository.save(invoice);
	}

	private ReservationEntity createReservation(InvoiceEntity invoice, TableEntity table, String customerName,
			String customerPhone) {
		EmployeeEntity defaultEmp = employeeRepository.findById(1)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên mặc định"));

		ReservationEntity reservation = new ReservationEntity();
		ReservationKey key = new ReservationKey();
		key.setIdInvoice(invoice.getId());
		key.setIdTable(table.getId());
		key.setIdEmployee(defaultEmp.getId());

		reservation.setId(key);
		reservation.setInvoice(invoice);
		reservation.setTable(table);
		reservation.setEmployee(defaultEmp);
		reservation.setCustomerName(customerName);
		reservation.setCustomerPhone(customerPhone);
		reservation.setReservationDate(LocalDate.now());
		reservation.setReservationTime(LocalDateTime.now().toLocalTime());
		reservation.setIsDeleted(false);

		return reservation;
	}
}
