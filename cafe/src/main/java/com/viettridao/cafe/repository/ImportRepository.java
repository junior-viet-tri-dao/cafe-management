package com.viettridao.cafe.repository;

// Import các thư viện cần thiết
import com.viettridao.cafe.dto.response.warehouse.WarehouseResponse;
import com.viettridao.cafe.model.ImportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository cho thực thể ImportEntity.
 * Chịu trách nhiệm truy vấn dữ liệu liên quan đến nhập hàng từ cơ sở dữ liệu.
 */
@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

    /**
     * Truy vấn danh sách nhập và xuất kho, bao gồm thông tin sản phẩm, ngày
     * nhập/xuất, số lượng, đơn vị và giá sản phẩm.
     *
     * @param keyword  Từ khóa tìm kiếm theo tên sản phẩm (có thể null).
     * @param pageable Đối tượng phân trang.
     * @return Trang kết quả chứa thông tin nhập và xuất kho.
     */
    @Query(value = """
            SELECT
                i.imports_id,
                NULL,
                p.product_name,
                i.import_date,
                NULL,
                i.quantity,
                NULL,
                u.unit_name,
                p.product_price
                from imports i
                join products p on i.product_id = p.product_id
                join units u on p.unit_id = u.unit_id
                WHERE (:keyword IS NULL OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%')))

            UNION ALL

            SELECT
                NULL,
                e.employee_id,
                p.product_name,
                NULL,
                e.export_date,
                NULL,
                e.quantity,
                u.unit_name,
                p.product_price
                from exports e
                join products p on e.product_id = p.product_id
                join units u on p.unit_id = u.unit_id
               WHERE (:keyword IS NULL OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            """, countQuery = """
                SELECT COUNT(*)
                FROM (
                    SELECT p.product_id
                    FROM imports i
                    JOIN products p ON i.product_id = p.product_id
                    WHERE (:keyword IS NULL OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%')))

                    UNION ALL

                    SELECT p.product_id
                    FROM exports e
                    JOIN products p ON e.product_id = p.product_id
                    WHERE (:keyword IS NULL OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%')))
                ) AS temp
            """, nativeQuery = true)
    Page<WarehouseResponse> getAllWarehouses(@Param("keyword") String keyword, Pageable pageable);
}
