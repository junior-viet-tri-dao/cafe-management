package com.viettridao.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {

    ReservationEntity findByInvoice(InvoiceEntity unpaidInvoice);

}
