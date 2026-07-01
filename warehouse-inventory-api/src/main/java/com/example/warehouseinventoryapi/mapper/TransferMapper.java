package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.request.CreateTransferRequest;
import com.example.warehouseinventoryapi.dto.response.TransferResponse;
import com.example.warehouseinventoryapi.entity.Product;
import com.example.warehouseinventoryapi.entity.Transfer;
import com.example.warehouseinventoryapi.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TransferMapper {

    public Transfer toEntityCreate(CreateTransferRequest request) {
        return Transfer.builder()
                .product(Product.builder().id(request.productId()).build())
                .sourceWarehouse(request.sourceWarehouseId() != null ?
                        Warehouse.builder().id(request.sourceWarehouseId()).build() : null)
                .destinationWarehouse(Warehouse.builder().id(request.destinationWarehouseId()).build())
                .quantity(request.quantity())
                .transferDate(LocalDateTime.now()) // Fecha seteada automáticamente al registrar el movimiento
                .build();
    }

    public TransferResponse toDto(Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getProduct() != null ? transfer.getProduct().getId() : null,
                transfer.getSourceWarehouse() != null ? transfer.getSourceWarehouse().getId() : null,
                transfer.getDestinationWarehouse() != null ? transfer.getDestinationWarehouse().getId() : null,
                transfer.getQuantity(),
                transfer.getTransferDate()
        );
    }

    public List<TransferResponse> toDtoList(List<Transfer> transfers) {
        return transfers.stream().map(this::toDto).toList();
    }

    public Page<TransferResponse> toDtoPage(Page<Transfer> page) {
        return page.map(this::toDto);
    }
}
