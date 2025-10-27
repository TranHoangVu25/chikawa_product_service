package com.example.product_service.services;

import com.example.product_service.configuration.RabbitMQConfig;
import com.example.product_service.dto.request.CartItemRequest;
import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.request.ProductSearchEvent;
import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.dto.response.ApiResponse;
import com.example.product_service.exception.ErrorCode;
import com.example.product_service.models.Category;
import com.example.product_service.models.CharacterEntity;
import com.example.product_service.models.Product;
import com.example.product_service.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    RabbitTemplate rabbitTemplate;
    public static final String CREATE_QUEUE = "search_create_queue";
    public static final String UPDATE_QUEUE = "search_update_queue";
    public static final String DELETE_QUEUE = "search_delete_queue";

//    @Override
//    public ApiResponse<Product> createProduct(CreateProductRequest request) {
//        if (productRepository.existsById(request.getId())){
//            throw new RuntimeException("id is existed");
//        }
//        Product product = Product.builder()
//                .id(request.getId())
//                .name(request.getName())
//                .description(request.getDescription())
//                .price(request.getPrice())
//                .status(request.getStatus())
//                .vendor(request.getVendor())
//                .images(request.getImages())
//                .variants(request.getVariants())
//                .categories(request.getCategories())
//                .characters(request.getCharacters())
//                .build();
//        return ApiResponse.<Product>builder()
//                .result(productRepository.save(product))
//                .build();
//    }

//    @Override
//    public ApiResponse<Product> updateProduct(String id, UpdateProductRequest request) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("In update. Product was not found"));
//        product.setName(request.getName());
//        product.setDescription(request.getDescription());
//        product.setPrice(request.getPrice());
//        product.setStatus(request.getStatus());
//        product.setVendor(request.getVendor());
//        product.setImages(request.getImages());
//        product.setVariants(request.getVariants());
//        product.setCategories(request.getCategories());
//        product.setCharacters(request.getCharacters());
//
//        return ApiResponse.<Product>builder()
//                .result(productRepository.save(product))
//                .build();
//    }

    @Override
    public ApiResponse<List<Product>> findAllProduct() {
        try {
            List<Product> products = productRepository.findAll();
            if (products.isEmpty()) {
                return ApiResponse.<List<Product>>builder()
                        .message("No product was found")
                        .result(List.of())
                        .build();
            }
            return ApiResponse.<List<Product>>builder()
                    .message("Get all products successfully")
                    .result(products)
                    .build();
        } catch (Exception e) {
            e.printStackTrace(); // Log stacktrace
            throw new RuntimeException("Error fetching products", e);
        }
    }

    @Override
    public ResponseEntity<ApiResponse<String>> deleteProduct(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(ErrorCode.PRODUCT_NOT_EXISTED.getMessage()));
        ProductSearchEvent e = ProductSearchEvent
                .builder()
                .id(id)
                .build();
        productRepository.deleteById(id);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                e
                );

        System.out.println("==Send id: " + id + "to RabbitMq");
        return ResponseEntity
                .ok()
                .body(
                        ApiResponse.<String>builder()
                                .message("Delete successfully")
                                .build());
    }

    //create product and send data to RabbitMq
    public ApiResponse<Product> createProduct(Product product) {
        if (!productRepository.existsById(product.getId())) {
            // Gửi event sang RabbitMQ
            ProductSearchEvent event = ProductSearchEvent.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .categories(product.getCategories().stream()
                            .map(Category::getName)
                            .toList())
                    .characters(product.getCharacters().stream()
                            .map(CharacterEntity::getName)
                            .toList())
                    .build();

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    event
            );
            System.out.println("Sent to RabbitMQ: " + event.getName());
            return ApiResponse.<Product>builder()
                    .result(productRepository.save(product))
                    .message("Create Product successfully")
                    .build();
        }
        return ApiResponse.<Product>builder()
                .message(ErrorCode.PRODUCT_EXISTED.getMessage())
                .code(ErrorCode.PRODUCT_EXISTED.getCode())
                .build();
    }

    //update product and send data to RabbitMq
    public ResponseEntity<ApiResponse<Product>> updateProduct(String id, UpdateProductRequest request) {
        try {
            Product existing = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException(ErrorCode.PRODUCT_NOT_EXISTED.getMessage()));

            existing.setName(request.getName());
            existing.setDescription(request.getDescription());
            existing.setPrice(request.getPrice());
            existing.setStatus(request.getStatus());
            existing.setVendor(request.getVendor());
            existing.setImages(request.getImages());
            existing.setVariants(request.getVariants());
            existing.setCategories(request.getCategories());
            existing.setCharacters(request.getCharacters());

            Product saved = productRepository.save(existing);

            // create event corresponding and send to RabbitMQ
            ProductSearchEvent event = ProductSearchEvent.builder()
                    .id(saved.getId())
                    .name(saved.getName())
                    .description(saved.getDescription())
                    .price(saved.getPrice())
                    .categories(Optional.ofNullable(saved.getCategories())
                            .orElse(List.of())
                            .stream()
                            .map(Category::getName)
                            .toList())
                    .characters(saved.getCharacters()
                            .stream().map(CharacterEntity::getName).toList())
                    .build();

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY,
                    event
            );

            System.out.println("Sent to RabbitMQ: " + event.getName());

            return ResponseEntity
                    .ok().body(
                            ApiResponse.<Product>builder()
                                    .result(saved)
                                    .message("Product updated and event published")
                                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
