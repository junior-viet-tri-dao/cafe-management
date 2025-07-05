package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableTransferService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableTransferServiceImpl implements TableTransferService {

	private final TableRepository tableRepository;
	private final InvoiceRepository invoiceRepository;
	private final ReservationRepository reservationRepository;

	@Override
	@Transactional
	public void transferTable(Integer fromTableId, Integer toTableId) {
		if (fromTableId.equals(toTableId)) {
			throw new IllegalArgumentException("Không thể chuyển sang cùng một bàn.");
		}

		TableEntity fromTable = tableRepository.findById(fromTableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn."));
		TableEntity toTable = tableRepository.findById(toTableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn đích."));

		TableStatus fromStatus = fromTable.getStatus();
		TableStatus toStatus = toTable.getStatus();

		if ((fromStatus == TableStatus.AVAILABLE
				&& (toStatus == TableStatus.RESERVED || toStatus == TableStatus.OCCUPIED))
				|| (fromStatus == TableStatus.RESERVED && toStatus != TableStatus.AVAILABLE)
				|| (fromStatus == TableStatus.OCCUPIED && toStatus != TableStatus.AVAILABLE)
				|| (fromStatus == TableStatus.RESERVED && toStatus == TableStatus.RESERVED)
				|| (fromStatus == TableStatus.OCCUPIED && toStatus == TableStatus.OCCUPIED)) {
			throw new RuntimeException("Chuyển bàn không hợp lệ .");
		}

		InvoiceEntity invoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(fromTableId,
						InvoiceStatus.UNPAID);

		if (invoice == null) {
			throw new RuntimeException("Không có hóa đơn chưa thanh toán ở bàn nguồn.");
		}

		InvoiceEntity targetInvoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(toTableId,
						InvoiceStatus.UNPAID);

		if (targetInvoice != null) {
			throw new RuntimeException("Bàn đích đang có hóa đơn chưa thanh toán.");
		}

		List<ReservationEntity> oldReservations = reservationRepository
				.findByInvoice_IdAndIsDeletedFalse(invoice.getId());

		for (ReservationEntity old : oldReservations) {
			reservationRepository.delete(old);

			ReservationEntity newReservation = new ReservationEntity();
			ReservationKey newKey = new ReservationKey();
			newKey.setIdInvoice(old.getInvoice().getId());
			newKey.setIdTable(toTableId);
			newKey.setIdEmployee(old.getEmployee().getId());

			newReservation.setId(newKey);
			newReservation.setInvoice(old.getInvoice());
			newReservation.setTable(toTable);
			newReservation.setEmployee(old.getEmployee());
			newReservation.setCustomerName(old.getCustomerName());
			newReservation.setCustomerPhone(old.getCustomerPhone());
			newReservation.setReservationDate(old.getReservationDate());
			newReservation.setReservationTime(old.getReservationTime());
			newReservation.setIsDeleted(false);

			reservationRepository.save(newReservation);
		}

		if (fromStatus == TableStatus.RESERVED && toStatus == TableStatus.AVAILABLE) {
			fromTable.setStatus(TableStatus.AVAILABLE);
			toTable.setStatus(TableStatus.RESERVED);
		} else if (fromStatus == TableStatus.OCCUPIED && toStatus == TableStatus.AVAILABLE) {
			fromTable.setStatus(TableStatus.AVAILABLE);
			toTable.setStatus(TableStatus.OCCUPIED);
		}

		tableRepository.save(fromTable);
		tableRepository.save(toTable);
	}
}
