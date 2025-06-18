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
public class ChiTietHoaDonId implements Serializable {
    private static final long serialVersionUID = 7619863463910023308L;
    @NotNull
    @Column(name = "MaThucDon", nullable = false)
    private Integer maThucDon;

    @NotNull
    @Column(name = "MaHoaDon", nullable = false)
    private Integer maHoaDon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChiTietHoaDonId entity = (ChiTietHoaDonId) o;
        return Objects.equals(this.maThucDon, entity.maThucDon) &&
                Objects.equals(this.maHoaDon, entity.maHoaDon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThucDon, maHoaDon);
    }

}