package com.example.product_service.services;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.models.Product;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    Product createProduct(CreateProductRequest request);
}
