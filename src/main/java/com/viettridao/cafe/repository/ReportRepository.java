package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceEntity;

@Repository
public interface ReportRepository extends JpaRepository<InvoiceEntity, Integer> {
    // Tổng doanh thu theo ngày từ hóa đơn đã thanh toán
    @Query("SELECT DATE(i.createdAt), SUM(i.totalAmount) FROM InvoiceEntity i WHERE i.isDeleted = false AND i.status = 'PAID' AND i.createdAt BETWEEN :start AND :end GROUP BY DATE(i.createdAt)")
    List<Object[]> sumRevenueByDate(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    // Tổng chi mua thiết bị theo ngày
    @Query("SELECT e.purchaseDate, SUM(e.purchasePrice) FROM EquipmentEntity e WHERE e.isDeleted = false AND e.purchaseDate BETWEEN :start AND :end GROUP BY e.purchaseDate")
    List<Object[]> sumDeviceExpenseByDate(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // Tổng chi nhập hàng theo ngày
    @Query("SELECT i.importDate, SUM(i.totalAmount) FROM ImportEntity i WHERE i.isDeleted = false AND i.importDate BETWEEN :start AND :end GROUP BY i.importDate")
    List<Object[]> sumImportExpenseByDate(@Param("start") LocalDate start, @Param("end") LocalDate end);

    // Tổng chi tiêu khác theo ngày
    @Query("SELECT e.expenseDate, SUM(e.amount) FROM ExpenseEntity e WHERE e.isDeleted = false AND e.expenseDate BETWEEN :start AND :end GROUP BY e.expenseDate")
    List<Object[]> sumOtherExpenseByDate(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
