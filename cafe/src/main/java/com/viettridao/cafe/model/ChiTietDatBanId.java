package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ChiTietDatBanId implements Serializable {
    private static final long serialVersionUID = 3055518461314698049L;
    @NotNull
    @Column(name = "MaBan", nullable = false)
    private Integer maBan;

    @NotNull
    @Column(name = "MaNhanVien", nullable = false)
    private Integer maNhanVien;

    @NotNull
    @Column(name = "MaHoaDon", nullable = false)
    private Integer maHoaDon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChiTietDatBanId entity = (ChiTietDatBanId) o;
        return Objects.equals(this.maNhanVien, entity.maNhanVien) &&
                Objects.equals(this.maBan, entity.maBan) &&
                Objects.equals(this.maHoaDon, entity.maHoaDon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maNhanVien, maBan, maHoaDon);
    }

}