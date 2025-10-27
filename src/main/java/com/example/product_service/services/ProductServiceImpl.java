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

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    RestTemplate restTemplate;
    String CART_SERVICE_URL = "http://localhost:8082/api/v1/cart";

    @Override
    public ApiResponse<Product> createProduct(CreateProductRequest request) {
        if (productRepository.existsById(request.getId())){
            throw new RuntimeException("id is existed");
        }
        Product product = Product.builder()
                .id(request.getId())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .status(request.getStatus())
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
    public ApiResponse<Product> updateProduct(String id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("In update. Product was not found"));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStatus(request.getStatus());
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
        try {
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
                    .build();        } catch (Exception e) {
            e.printStackTrace(); // Log stacktrace
            throw new RuntimeException("Error fetching products", e);
        }


    }

    @Override
    public ApiResponse<String> deleteProduct(String id) {
        if(!productRepository.existsById(id)){
            return ApiResponse.<String>builder()
                    .message("Product Id not found")
                    .build();
        }
        productRepository.deleteById(id);
        return ApiResponse.<String>builder()
                .message("Delete successful")
                .build();
    }

    private final RabbitTemplate rabbitTemplate;

    public ApiResponse<Product> createProduct_rabbit(Product product) {
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
            System.out.println("📤 Sent to RabbitMQ: " + event.getName());
            return ApiResponse.<Product>builder()
                    .result(productRepository.save(product))
                    .message("Create Product successfully")
                    .build();
        }
        return ApiResponse.<Product>builder()
                .message(ErrorCode.PRODUCT_EXISTED.getMessage())
                .code(ErrorCode.PRODUCT_EXISTED.getCode())
                .statusCode(ErrorCode.PRODUCT_EXISTED.getHttpStatusCode())
                .build();
    }
}
