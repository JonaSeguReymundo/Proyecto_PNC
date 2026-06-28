package com.example.warehouseinventoryapi.entity;

public enum ReservationStatus {
    ACTIVE,     // reserva vigente, stock apartado
    CONFIRMED,  // confirmada: el stock salio de verdad
    RELEASED,   // liberada manualmente: stock devuelto
    EXPIRED     // vencida por timeout: stock devuelto automaticamente
}