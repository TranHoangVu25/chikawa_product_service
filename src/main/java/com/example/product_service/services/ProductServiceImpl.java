package com.example.product_service.services;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.models.Product;
import com.example.product_service.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
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
                .images(request.getImages())
                .variants(request.getVariants())
                .categories(request.getCategories())
                .characters(request.getCharacters())
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String productId, UpdateProductRequest request) {
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

        return productRepository.save(product);
    }

    @Override
    public List<Product> findAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(String productId) {
        productRepository.deleteById(productId);
    }
}
