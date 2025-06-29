// java.util.List<ImportEntity> findAllByImportDateBetween(LocalDate from, LocalDate to);
package com.viettridao.cafe.repository;

// import com.viettridao.cafe.dto.response.warehouse.WareHouseResponse;
import com.viettridao.cafe.model.ImportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

    // Tìm các phiếu nhập theo tên sản phẩm (nếu có keyword)
    Page<ImportEntity> findByProduct_ProductNameContainingIgnoreCaseAndIsDeletedFalse(String keyword,
            Pageable pageable);

    // Tìm tất cả phiếu nhập nếu không có keyword
    Page<ImportEntity> findByIsDeletedFalse(Pageable pageable);
}
