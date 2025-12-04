package com.example.product_service.services;

import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.dto.response.ApiResponse;
import com.example.product_service.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
//    ApiResponse<Product> createProduct(CreateProductRequest request);

    ResponseEntity<ApiResponse<Product>> updateProduct(String productId, UpdateProductRequest request);

    ApiResponse<List<Product>> findAllProduct();

    ResponseEntity<ApiResponse<String>> deleteProduct(String productId);

    ApiResponse<Product> createProduct(Product product);

    void createListProduct(List<Product> products);
}
