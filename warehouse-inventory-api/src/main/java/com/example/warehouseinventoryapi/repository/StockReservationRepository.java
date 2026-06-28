package com.example.warehouseinventoryapi.repository;

import com.example.warehouseinventoryapi.entity.ReservationStatus;
import com.example.warehouseinventoryapi.entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {

    List<StockReservation> findByStatus(ReservationStatus status);

    List<StockReservation> findByStatusAndExpiresAtBefore(ReservationStatus status, LocalDateTime time);
}