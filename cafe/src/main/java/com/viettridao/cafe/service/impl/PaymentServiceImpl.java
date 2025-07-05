package com.viettridao.cafe.service.impl;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.InvoiceItemDetailRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final InvoiceRepository invoiceRepository;
	private final InvoiceItemDetailRepository invoiceItemDetailRepository;
	private final TableRepository tableRepository;
	private final ReservationRepository reservationRepository;

	@Transactional
	@Override
	public PaymentResponse processPayment(PaymentRequest request) {
		Integer tableId = request.getTableId();
		Double customerCash = request.getCustomerCash();

		InvoiceEntity invoice = invoiceRepository
				.findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(tableId,
						InvoiceStatus.UNPAID);

		if (invoice == null) {
			return new PaymentResponse(false, "Không tìm thấy hóa đơn chưa thanh toán", null, null, null, null,
					"NOT_FOUND", null, null);
		}

		List<InvoiceDetailEntity> items = invoiceItemDetailRepository
				.findByInvoice_IdAndIsDeletedFalse(invoice.getId());

		if (items == null || items.isEmpty()) {
			return new PaymentResponse(false, "Hóa đơn không có món nào", 0.0, customerCash, customerCash,
					invoice.getId(), invoice.getStatus().name(), null, null);
		}

		double totalAmount = items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

		if (customerCash < totalAmount) {
			return new PaymentResponse(false, "Số tiền khách đưa không đủ để thanh toán", totalAmount, customerCash,
					null, invoice.getId(), invoice.getStatus().name(), null, null);
		}

		EmployeeEntity employee = invoice.getReservations().stream()
				.filter(r -> r.getIsDeleted() == null || !r.getIsDeleted())
				.max(Comparator.comparing(ReservationEntity::getReservationDate)
						.thenComparing(ReservationEntity::getReservationTime))
				.map(ReservationEntity::getEmployee).orElse(null);

		String paidByName = (employee != null) ? employee.getFullName() : "Không rõ";
		Integer paidById = (employee != null) ? employee.getId() : null;

		invoice.setStatus(InvoiceStatus.PAID);
		invoice.setTotalAmount(totalAmount);
		invoiceRepository.save(invoice);

		List<ReservationEntity> reservations = reservationRepository.findByInvoice_IdAndIsDeletedFalse(invoice.getId());
		for (ReservationEntity r : reservations) {
			r.setIsDeleted(true);
		}
		reservationRepository.saveAll(reservations);

		if (Boolean.TRUE.equals(request.getFreeTable())) {
			TableEntity table = tableRepository.findById(tableId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn có ID: " + tableId));
			table.setStatus(TableStatus.AVAILABLE);
			tableRepository.save(table);
		}

		double change = customerCash - totalAmount;

		return new PaymentResponse(true, "Thanh toán thành công", totalAmount, customerCash, change, invoice.getId(),
				invoice.getStatus().name(), paidByName, paidById);
	}
}
