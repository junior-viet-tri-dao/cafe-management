// java.util.List<ImportEntity> findAllByImportDateBetween(LocalDate from, LocalDate to);
package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.response.warehouse.WareHouseResponse;
import com.viettridao.cafe.model.ImportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {
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
    Page<WareHouseResponse> getAllWarehouses(@Param("keyword") String keyword, Pageable pageable);
}
