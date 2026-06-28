package com.example.warehouseinventoryapi.entity;

public enum MovementType {
    ENTRY_PURCHASE,      // entrada por compra
    EXIT_ORDER,          // salida por pedido/orden
    EXIT_TRANSFER,       // salida por traslado
    RESERVATION,         // reserva de stock
    RELEASE_RESERVATION, // liberacion de reserva
    ADJUSTMENT           // ajuste manual
}