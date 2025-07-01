package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
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
import com.viettridao.cafe.service.TableMergeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableMergeServiceImpl implements TableMergeService {

	private final TableRepository tableRepository;
	private final InvoiceRepository invoiceRepository;
	private final InvoiceDetailRepository invoiceDetailRepository;
	private final ReservationRepository reservationRepository;
	private final EmployeeRepository employeeRepository;

	@Override
	@Transactional
	public void mergeTables(Integer targetTableId, List<Integer> sourceTableIds, String customerName,
			String customerPhone) {
		TableEntity targetTable = tableRepository.findById(targetTableId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn gộp đến."));

		TableStatus targetStatus = targetTable.getStatus();

		if (targetStatus == TableStatus.RESERVED) {
			throw new RuntimeException("Không thể gộp vào bàn đã đặt trước.");
		}

		InvoiceEntity targetInvoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(targetTableId,
						InvoiceStatus.UNPAID);

		// Nếu bàn đích là AVAILABLE thì tạo mới hóa đơn + reservation
		if (targetStatus == TableStatus.AVAILABLE) {
			String finalName = customerName;
			String finalPhone = customerPhone;

			if (sourceTableIds.size() == 1) {
				Integer sourceId = sourceTableIds.get(0);
				InvoiceEntity sourceInvoice = invoiceRepository
						.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(sourceId,
								InvoiceStatus.UNPAID);
				if (sourceInvoice != null) {
					List<ReservationEntity> srcReservations = reservationRepository
							.findByInvoice_IdAndIsDeletedFalse(sourceInvoice.getId());
					if (!srcReservations.isEmpty()) {
						finalName = srcReservations.get(0).getCustomerName();
						finalPhone = srcReservations.get(0).getCustomerPhone();
					}
				}
			}

			targetInvoice = new InvoiceEntity();
			targetInvoice.setStatus(InvoiceStatus.UNPAID);
			targetInvoice.setCreatedAt(LocalDateTime.now());
			targetInvoice.setIsDeleted(false);
			targetInvoice.setTotalAmount(0.0);
			invoiceRepository.save(targetInvoice);

			EmployeeEntity employee = employeeRepository.findById(1)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));

			ReservationEntity reservation = new ReservationEntity();
			ReservationKey key = new ReservationKey();
			key.setIdInvoice(targetInvoice.getId());
			key.setIdTable(targetTableId);
			key.setIdEmployee(employee.getId());

			reservation.setId(key);
			reservation.setInvoice(targetInvoice);
			reservation.setTable(targetTable);
			reservation.setEmployee(employee);
			reservation.setCustomerName(finalName);
			reservation.setCustomerPhone(finalPhone);
			reservation.setReservationDate(LocalDate.now());
			reservation.setReservationTime(LocalTime.now());
			reservation.setIsDeleted(false);
			reservationRepository.save(reservation);

			targetTable.setStatus(TableStatus.OCCUPIED);
			tableRepository.save(targetTable);
		} else if (targetStatus == TableStatus.OCCUPIED && targetInvoice == null) {
			throw new RuntimeException("Bàn đang phục vụ nhưng chưa có hóa đơn.");
		}

		long occupiedSourceCount = sourceTableIds.stream()
				.map(id -> tableRepository.findById(id).orElse(null))
				.filter(t -> t != null && t.getStatus() == TableStatus.OCCUPIED)
				.count();

		if (targetStatus == TableStatus.OCCUPIED && targetInvoice != null && occupiedSourceCount > 1) {
			List<ReservationEntity> reservations = reservationRepository
					.findByInvoice_IdAndIsDeletedFalse(targetInvoice.getId());
			for (ReservationEntity r : reservations) {
				r.setCustomerName(customerName);
				r.setCustomerPhone(customerPhone);
			}
			reservationRepository.saveAll(reservations);
		}

		Map<Integer, InvoiceDetailEntity> mergedDetails = new HashMap<>();
		if (targetInvoice.getInvoiceDetails() != null) {
			for (InvoiceDetailEntity detail : targetInvoice.getInvoiceDetails()) {
				if (Boolean.TRUE.equals(detail.getIsDeleted()))
					continue;
				mergedDetails.put(detail.getMenuItem().getId(), detail);
			}
		}

		for (Integer sourceId : sourceTableIds) {
			if (sourceId.equals(targetTableId))
				continue;

			TableEntity sourceTable = tableRepository.findById(sourceId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn."));

			if (sourceTable.getStatus() == TableStatus.RESERVED) {
				throw new RuntimeException("Không thể gộp từ bàn đã đặt trước.");
			}

			InvoiceEntity sourceInvoice = invoiceRepository
					.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(sourceId,
							InvoiceStatus.UNPAID);

			if (sourceInvoice == null || sourceInvoice.getInvoiceDetails() == null)
				continue;

			for (InvoiceDetailEntity detail : sourceInvoice.getInvoiceDetails()) {
				if (Boolean.TRUE.equals(detail.getIsDeleted()))
					continue;

				Integer menuItemId = detail.getMenuItem().getId();
				if (mergedDetails.containsKey(menuItemId)) {
					InvoiceDetailEntity existing = mergedDetails.get(menuItemId);
					existing.setQuantity(existing.getQuantity() + detail.getQuantity());
					invoiceDetailRepository.save(existing);
				} else {
					InvoiceDetailEntity newDetail = new InvoiceDetailEntity();
					InvoiceKey newKey = new InvoiceKey();
					newKey.setIdInvoice(targetInvoice.getId());
					newKey.setIdMenuItem(menuItemId);

					newDetail.setId(newKey);
					newDetail.setInvoice(targetInvoice);
					newDetail.setMenuItem(detail.getMenuItem());
					newDetail.setQuantity(detail.getQuantity());
					newDetail.setPrice(detail.getPrice());
					newDetail.setIsDeleted(false);

					invoiceDetailRepository.save(newDetail);
					mergedDetails.put(menuItemId, newDetail);
				}

				detail.setIsDeleted(true);
				invoiceDetailRepository.save(detail);
			}

			sourceInvoice.setIsDeleted(true);
			invoiceRepository.save(sourceInvoice);

			List<ReservationEntity> oldReservations = reservationRepository
					.findByInvoice_IdAndIsDeletedFalse(sourceInvoice.getId());
			for (ReservationEntity old : oldReservations) {
				old.setIsDeleted(true);
			}
			reservationRepository.saveAll(oldReservations);

			sourceTable.setStatus(TableStatus.AVAILABLE);
			tableRepository.save(sourceTable);
		}

		double totalAmount = mergedDetails.values().stream().filter(d -> d.getIsDeleted() == null || !d.getIsDeleted())
				.mapToDouble(d -> d.getQuantity() * d.getPrice()).sum();

		targetInvoice.setTotalAmount(totalAmount);
		invoiceRepository.save(targetInvoice);
	}
}
