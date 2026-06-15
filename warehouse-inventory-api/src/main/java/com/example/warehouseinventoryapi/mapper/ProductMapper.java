package com.example.warehouseinventoryapi.mapper;

import com.example.warehouseinventoryapi.dto.request.CreateProductRequest;
import com.example.warehouseinventoryapi.dto.request.UpdateProductRequest;
import com.example.warehouseinventoryapi.dto.response.ProductResponse;
import com.example.warehouseinventoryapi.entity.Product;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ProductMapper {

    public Product toEntityCreate(CreateProductRequest request) {
        return Product.builder()
                .sku(request.sku())
                .name(request.name())
                .description(request.description())
                .weight(request.weight())
                .length(request.length())
                .width(request.width())
                .height(request.height())
                .minimumStock(request.minimumStock())
                .active(true)
                .build();
    }

    public Product toEntityUpdate(UpdateProductRequest request, Long id) {
        return Product.builder()
                .id(id)
                .name(request.name())
                .description(request.description())
                .weight(request.weight())
                .length(request.length())
                .width(request.width())
                .height(request.height())
                .minimumStock(request.minimumStock())
                .active(request.active())
                .build();
    }

    public ProductResponse toDto(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getWeight(),
                product.getLength(),
                product.getWidth(),
                product.getHeight(),
                product.getMinimumStock(),
                product.getActive()
        );
    }

    public List<ProductResponse> toDtoList(List<Product> products) {
        return products.stream().map(this::toDto).toList();
    }
}
