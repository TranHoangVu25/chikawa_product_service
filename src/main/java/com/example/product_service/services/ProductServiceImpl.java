package com.example.product_service.services;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.dto.response.ApiResponse;
import com.example.product_service.models.Product;
import com.example.product_service.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;

    @Override
    public ApiResponse<Product> createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .product_id(request.getProduct_id())
                .url(request.getUrl())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .vendor(request.getVendor())
                .images(request.getImages())
                .variants(request.getVariants())
                .categories(request.getCategories())
                .characters(request.getCharacters())
                .build();
        return ApiResponse.<Product>builder()
                .result(productRepository.save(product))
                .build();
    }

    @Override
    public ApiResponse<Product> updateProduct(String productId, UpdateProductRequest request) {
        Product product = productRepository.findByProduct_Id(productId)
                .orElseThrow(() -> new RuntimeException("In update. Product was not found"));
        product.setUrl(request.getUrl());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setVendor(request.getVendor());
        product.setImages(request.getImages());
        product.setVariants(request.getVariants());
        product.setCategories(request.getCategories());
        product.setCharacters(request.getCharacters());

        return ApiResponse.<Product>builder()
                .result(productRepository.save(product))
                .build();
    }

    @Override
    public ApiResponse<List<Product>> findAllProduct() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()){
            return ApiResponse.<List<Product>>builder()
                    .message("No product was found")
                    .result(List.of())
                    .build();
        }
        return ApiResponse.<List<Product>>builder()
                .message("Get all products successfully")
                .result(products)
                .build();
    }

    @Override
    public ApiResponse<String> deleteProduct(String productId) {
        if(!productRepository.existsByProductId(productId)){
            return ApiResponse.<String>builder()
                    .message("Product Id not found")
                    .build();
        }
        productRepository.deleteByProduct_Id(productId);
        return ApiResponse.<String>builder()
                .message("Delete successful")
                .build();
    }

    @Override
    public ApiResponse<String> addToCart(Integer userId, String jwt) {
        return null;
    }
}
