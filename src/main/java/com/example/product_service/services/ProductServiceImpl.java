package com.example.product_service.services;

import com.example.product_service.Enums.Action;
import com.example.product_service.configuration.RabbitMQConfig;
import com.example.product_service.dto.BestSellersDTO;
import com.example.product_service.dto.NewArrivalsDTO;
import com.example.product_service.dto.request.ProductSendEvent;
import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.dto.response.ApiResponse;
import com.example.product_service.dto.response.PageResponse;
import com.example.product_service.exception.ErrorCode;
import com.example.product_service.models.Category;
import com.example.product_service.models.CharacterEntity;
import com.example.product_service.dto.HomeProductDTO;
import com.example.product_service.models.Product;
import com.example.product_service.repositories.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    RabbitTemplate rabbitTemplate;

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

//    @Override
//    public ApiResponse<PageResponse<Product>> findAllProduct(int page) {
//        try {
//            int size = 8; // FIX CỨNG 8 SẢN PHẨM / PAGE
//
//            Pageable pageable = PageRequest.of(
//                    page,
//                    size,
//                    Sort.by("createdAt").descending()
//            );
//
//            Page<Product> productPage = productRepository.findAll(pageable);
//
//            if (productPage.isEmpty()) {
//                return ApiResponse.<PageResponse<Product>>builder()
//                        .message("No product was found")
//                        .result(
//                                PageResponse.<Product>builder()
//                                        .data(List.of())
//                                        .page(page)
//                                        .size(size)
//                                        .totalElements(0)
//                                        .totalPages(0)
//                                        .last(true)
//                                        .build()
//                        )
//                        .build();
//            }
//
//            PageResponse<Product> pageResponse = PageResponse.<Product>builder()
//                    .data(productPage.getContent())
//                    .page(productPage.getNumber())
//                    .size(productPage.getSize())
//                    .totalElements(productPage.getTotalElements())
//                    .totalPages(productPage.getTotalPages())
//                    .last(productPage.isLast())
//                    .build();
//
//            return ApiResponse.<PageResponse<Product>>builder()
//                    .message("Get products successfully")
//                    .result(pageResponse)
//                    .build();
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching products", e);
//        }
//    }

    @Override
    public ResponseEntity<ApiResponse<String>> deleteProduct(String id) {
        Action action = Action.DELETE;
        productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException(ErrorCode.PRODUCT_NOT_EXISTED.getMessage()));
        ProductSendEvent e = ProductSendEvent
                .builder()
                .id(id)
                .action(action)
                .build();
        productRepository.deleteById(id);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
//                RabbitMQConfig.ROUTING_KEY,
                "",
                e
                );

        System.out.println("In delete request. Send id: " + id + "to RabbitMq");
        return ResponseEntity
                .ok()
                .body(
                        ApiResponse.<String>builder()
                                .message("Delete successfully")
                                .build());
    }

    //create product and send data to RabbitMq
    public ApiResponse<Product> createProduct(Product product) {
        Action action = Action.CREATE;
        String productId = UUID.randomUUID().toString();
        if (!productRepository.existsById(productId)) {
            // Gửi event sang RabbitMQ
            ProductSendEvent event = ProductSendEvent.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .status(product.getStatus())
                    .categories(product.getCategories().stream()
                            .map(Category::getName)
                            .toList())
                    .characters(product.getCharacters().stream()
                            .map(CharacterEntity::getName)
                            .toList())
                    .images(product.getImages().stream().toList())
                    .action(action)
                    .build();

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
//                    RabbitMQConfig.ROUTING_KEY,   routing key cho kiểu topic
                    "", //routing key cho fanout là ""
                    event
            );
            System.out.println("Creation request. Sent to RabbitMQ: " + event.getName());
            return ApiResponse.<Product>builder()
                    //save product in db
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
            Action action = Action.UPDATE;
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

            Product saved_product = productRepository.save(existing);

            // create event corresponding and send to RabbitMQ
            ProductSendEvent event = ProductSendEvent.builder()
                    .id(saved_product.getId())
                    .name(saved_product.getName())
                    .price(saved_product.getPrice())
                    .status(saved_product.getStatus())
                    .categories(Optional.ofNullable(saved_product.getCategories())
                            .orElse(List.of())
                            .stream()
                            .map(Category::getName)
                            .toList())
                    .characters(saved_product.getCharacters()
                            .stream().map(CharacterEntity::getName).toList())
                    .images(saved_product.getImages().stream().toList())
                    .action(action)
                    .build();

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
//                    RabbitMQConfig.ROUTING_KEY,
                    "",
                    event
            );
            System.out.println("Update Product.");
            System.out.println("Sent to RabbitMQ: " + event.getName());

            return ResponseEntity
                    .ok().body(
                            ApiResponse.<Product>builder()
                                    .result(saved_product)
                                    .message("Product updated and event published")
                                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //Lưu nhiều product
    @Override
    public void createListProduct(List<Product> products) {
        productRepository.saveAll(products);
    }

    //lấy các prod cho home
    @Override
    public ResponseEntity<ApiResponse<HomeProductDTO>> getHomeProduct() {
        try{
        List<Product> bestSellers = productRepository.findRandom10Products();
        List<Product> newArrivals = productRepository.findTop10ByOrderByCreatedAtDesc();

        BestSellersDTO best = new BestSellersDTO().builder()
//                .id(UUID.randomUUID().toString())
                .id("101")
                .name("Best Seller")
                .description("Best selling products")
                .slug("best-seller")
                .products(bestSellers)
                .build();

        NewArrivalsDTO newArrivalsDTO = new NewArrivalsDTO().builder()
//                .id(UUID.randomUUID().toString())
                .id("102")
                .name("New Arrival")
                .description("New arrival products")
                .slug("new-arrival")
                .products(newArrivals)
                .build();
        HomeProductDTO homeProduct = new HomeProductDTO().builder()
                .bestSellersDTO(best)
                .newArrivalsDTO(newArrivalsDTO)
                .build();
        return ResponseEntity.ok()
                .body(ApiResponse.<HomeProductDTO>builder()
                        .message("Get homepage products successfully")
                        .result(homeProduct)
                        .build());
    }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<HomeProductDTO>builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @Override
    public ResponseEntity<ApiResponse<Product>> getProductById(String productId) {
        try {

        if(productRepository.findById(productId).isEmpty()){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<Product>builder()
                            .code(ErrorCode.PRODUCT_NOT_EXISTED.getCode())
                            .message(ErrorCode.PRODUCT_NOT_EXISTED.getMessage())
                            .build());
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException(ErrorCode.PRODUCT_NOT_EXISTED.getMessage()));

        return ResponseEntity.ok()
                .body(ApiResponse.<Product>builder()
                        .message("Get product:"+product.getId())
                        .result(product)
                        .build());
    }
    catch (Exception e){
            log.error(e.getMessage());
        return ResponseEntity.ok()
            .body(ApiResponse.<Product>builder()
                    .message(e.getMessage())
                    .build());
    }
    }
}
