package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.NhanVien;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {

	Optional<NhanVien> findByUserAndPass(String user, String pass);

	@Query("SELECT n FROM NhanVien n WHERE n.daXoa = false")
	List<NhanVien> findAllActive();

	@Query("""
			    SELECT n FROM NhanVien n
			    WHERE n.daXoa = false AND (
			        LOWER(n.hoVaTen) LIKE LOWER(CONCAT('%', :kw, '%')) OR
			        LOWER(n.chucVu) LIKE LOWER(CONCAT('%', :kw, '%')) OR
			        n.sdt LIKE CONCAT('%', :kw, '%')
			    )
			""")
	List<NhanVien> findByKeyword(@Param("kw") String keyword);
}
