package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {

	// Tìm tất cả đặt bàn chưa xóa theo ID bàn
	List<ReservationEntity> findAllByTable_IdAndIsDeletedFalse(Integer tableId);

	// Tìm tất cả đặt bàn theo ID hóa đơn
	List<ReservationEntity> findAllByInvoice_IdAndIsDeletedFalse(Integer invoiceId);

	// ✅ Lấy bản đặt bàn mới nhất mà không cần @Query
	ReservationEntity findTopByTable_IdAndIsDeletedFalseOrderByReservationDateDesc(Integer tableId);
}
