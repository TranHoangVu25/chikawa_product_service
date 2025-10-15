package com.example.product_service.services;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.dto.response.ApiResponse;
import com.example.product_service.models.Product;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ApiResponse<Product> createProduct(CreateProductRequest request);

    ApiResponse<Product> updateProduct(String productId,UpdateProductRequest request);

    List<Product> findAllProduct();

    void deleteProduct(String productId);
}
