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
public class ChiTietThucDonId implements Serializable {
    private static final long serialVersionUID = -2045316036977155170L;
    @NotNull
    @Column(name = "MaHangHoa", nullable = false)
    private Integer maHangHoa;

    @NotNull
    @Column(name = "MaThucDon", nullable = false)
    private Integer maThucDon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChiTietThucDonId entity = (ChiTietThucDonId) o;
        return Objects.equals(this.maThucDon, entity.maThucDon) &&
                Objects.equals(this.maHangHoa, entity.maHangHoa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maThucDon, maHangHoa);
    }

}