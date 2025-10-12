package com.example.product_service.services;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.models.Product;
import com.example.product_service.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService{
    ProductRepository productRepository;
    @Override
    public Product createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .product_id(request.getProduct_id())
                .url(request.getUrl())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .vendor(request.getVendor())

                .build();
        return null;
    }
}
