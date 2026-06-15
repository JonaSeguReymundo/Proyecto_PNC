package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.request.CreateProductLocationRequest;
import com.example.warehouseinventoryapi.dto.response.AisleResponse;
import com.example.warehouseinventoryapi.dto.response.ProductLocationResponse;
import com.example.warehouseinventoryapi.entity.Aisle;
import com.example.warehouseinventoryapi.entity.Location;
import com.example.warehouseinventoryapi.entity.Product;
import com.example.warehouseinventoryapi.entity.ProductLocation;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ProductLocationMapper {

    public ProductLocation toEntityCreate(CreateProductLocationRequest request) {
        return ProductLocation.builder()
                .product(Product.builder().id(request.productId()).build())
                .location(Location.builder().id(request.locationId()).build())
                .quantity(request.quantity())
                .build();
    }

    public ProductLocationResponse toDto(ProductLocation productLocation) {
        return new ProductLocationResponse(
                productLocation.getId(),
                productLocation.getProduct() != null ? productLocation.getProduct().getId() : null,
                productLocation.getLocation() != null ? productLocation.getLocation().getId() : null,
                productLocation.getQuantity()
        );
    }

    public List<ProductLocationResponse> toDtoList(List<ProductLocation> productLocations) {
        return productLocations.stream().map(this::toDto).toList();
    }

    public Page<ProductLocationResponse> toDtoPage(Page<ProductLocation> page) {
        return page.map(this::toDto);
    }
}