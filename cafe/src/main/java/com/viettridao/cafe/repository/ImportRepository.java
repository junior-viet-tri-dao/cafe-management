package com.viettridao.cafe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.dto.response.warehouse.WareHouseResponse;
import com.viettridao.cafe.model.ImportEntity;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

	@Query(value = """
		    SELECT
		        i.imports_id AS importId,
		        NULL AS exportId,
		        p.product_name AS productName,
		        i.import_date AS importDate,
		        NULL AS exportDate,
		        i.quantity AS quantityImport,
		        NULL AS quantityExport,
		        u.unit_name AS unitName,
		        p.product_price AS productPrice
		    FROM imports i
		    JOIN products p ON i.product_id = p.product_id
		    JOIN units u ON p.unit_id = u.unit_id
		    WHERE (:keyword IS NULL OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%')))

		    UNION ALL

		    SELECT
		        NULL AS importId,
		        e.exports_id AS exportId, -- ✅ fix chỗ này
		        p.product_name AS productName,
		        NULL AS importDate,
		        e.export_date AS exportDate,
		        NULL AS quantityImport,
		        e.quantity AS quantityExport,
		        u.unit_name AS unitName,
		        p.product_price AS productPrice
		    FROM exports e
		    JOIN products p ON e.product_id = p.product_id
		    JOIN units u ON p.unit_id = u.unit_id
		    WHERE (:keyword IS NULL OR LOWER(p.product_name) LIKE LOWER(CONCAT('%', :keyword, '%')))
		    """,
		    countQuery = """
		    SELECT COUNT(*) FROM (
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
		    """,
		    nativeQuery = true)
		Page<WareHouseResponse> getAllWarehouses(@Param("keyword") String keyword, Pageable pageable);

	 
	 
    // Truy vấn đơn nhập có chứa từ khóa trong tên hàng hóa
    Page<ImportEntity> findByProduct_ProductNameContainingIgnoreCase(String keyword, Pageable pageable);

}
