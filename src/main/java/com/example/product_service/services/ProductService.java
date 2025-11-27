package com.example.product_service.services;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.dto.response.ApiResponse;
import com.example.product_service.dto.response.PageResponse;
import com.example.product_service.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    ApiResponse<Product> getProductById(String id);

    ApiResponse<Product> createProduct(CreateProductRequest request);

    ApiResponse<Product> updateProduct(String productId,UpdateProductRequest request);

//    ApiResponse<List<Product>> findAllProduct();

    ApiResponse<PageResponse<Product>> findAllProduct(int page, int size, String sort);


    ApiResponse<String> deleteProduct(String productId);

    Product createProduct_rabbit(Product product);
}
