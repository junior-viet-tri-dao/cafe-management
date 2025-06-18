package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "ChiTietThucDon", schema = "dbo")
public class ChiTietThucDon {
    @EmbeddedId
    private ChiTietThucDonId id;

    @MapsId("maHangHoa")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaHangHoa", nullable = false)
    private HangHoa maHangHoa;

    @MapsId("maThucDon")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MaThucDon", nullable = false)
    private ThucDon maThucDon;

    @NotNull
    @Column(name = "KhoiLuong", nullable = false, precision = 18, scale = 2)
    private BigDecimal khoiLuong;

    @Size(max = 50)
    @Nationalized
    @Column(name = "DonViTinh", length = 50)
    private String donViTinh;

}