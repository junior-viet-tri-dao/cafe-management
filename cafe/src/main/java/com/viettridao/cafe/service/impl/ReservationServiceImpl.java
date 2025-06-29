package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.repository.ReservationRepository;
import com.viettridao.cafe.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;

    @Override
    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAllByIsDeletedFalse();
    }

    @Override
    public List<ReservationEntity> getReservationsByTable(Integer tableId) {
        return reservationRepository.findAllByTable_IdAndIsDeletedFalse(tableId);
    }

    @Transactional
    @Override
    public ReservationEntity createReservation(ReservationEntity reservation) {
        reservation.setIsDeleted(false);
        return reservationRepository.save(reservation);
    }

    @Transactional
    @Override
    public void deleteReservation(ReservationEntity reservation) {
        reservation.setIsDeleted(true);
        reservationRepository.save(reservation);
    }
}
