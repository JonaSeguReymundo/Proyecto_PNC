package com.example.warehouseinventoryapi.service;

import com.example.warehouseinventoryapi.dto.request.CreateReservationRequest;
import com.example.warehouseinventoryapi.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationService {

    ReservationResponse create(CreateReservationRequest request);

    ReservationResponse confirm(Long id, String reference);

    ReservationResponse release(Long id);

    ReservationResponse getById(Long id);

    List<ReservationResponse> getActive();

    int releaseExpired();
}