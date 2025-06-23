package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.menu.MenuDTO;
import com.viettridao.cafe.model.ThucDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThucDonRepository extends JpaRepository<ThucDon, Integer> {
    @Query("""
    SELECT new com.viettridao.cafe.dto.menu.MenuDTO(
        t.maThucDon, t.tenMon, t.giaTienHienTai) FROM ThucDon t
    """)
    List<MenuDTO> getAllMenus();
}
