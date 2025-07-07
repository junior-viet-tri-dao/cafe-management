package com.viettridao.cafe.repository;

// Import các thư viện cần thiết
import com.viettridao.cafe.model.PromotionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

/**
 * Repository cho thực thể PromotionEntity.
 * Chịu trách nhiệm truy vấn dữ liệu liên quan đến khuyến mãi (Promotion) từ cơ
 * sở dữ liệu.
 */
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {

    // @Query("select e from PromotionEntity e where e.isDeleted = false")
    // Page<PromotionEntity> getAllPromotionByPage(Pageable pageable);

    /**
     * Lấy tất cả các khuyến mãi chưa bị xóa.
     *
     * @param pageable Đối tượng phân trang.
     * @return Trang kết quả chứa danh sách khuyến mãi chưa bị xóa.
     */
    Page<PromotionEntity> findByIsDeletedFalse(Pageable pageable);

    /**
     * Lấy danh sách các khuyến mãi chưa bị xóa và còn hiệu lực (đang active).
     *
     * @param now      Ngày hiện tại để kiểm tra hiệu lực khuyến mãi.
     * @param pageable Đối tượng phân trang.
     * @return Trang kết quả chứa danh sách khuyến mãi đang active.
     */
    @Query("SELECT p FROM PromotionEntity p WHERE p.isDeleted = false AND p.startDate <= :now AND p.endDate >= :now")
    Page<PromotionEntity> findActivePromotions(LocalDate now, Pageable pageable);

}
