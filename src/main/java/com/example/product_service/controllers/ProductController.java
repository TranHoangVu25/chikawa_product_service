package com.example.product_service.controllers;

import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.dto.response.ApiResponse;
import com.example.product_service.dto.HomeProductDTO;
import com.example.product_service.dto.response.PageResponse;
import com.example.product_service.models.Product;
import com.example.product_service.services.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
@Slf4j
public class ProductController {
    ProductService productService;

//    @GetMapping("/all")
//    public ApiResponse<PageResponse<Product>> getProducts(
//            @RequestParam(defaultValue = "0") int page
//    ) {
//        return productService.findAllProduct(page);
//    }

    @GetMapping("/all")
    public ApiResponse<List<Product>> getAllProduct() {
        return productService.findAllProduct();
    }

    //delete theo product id
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable String productId) {
        return productService.deleteProduct(productId);
    }

    //update for id and sent to RabbitMQ
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable String productId,
            @RequestBody UpdateProductRequest request
    ) {
        return productService.updateProduct(productId, request);
    }

    //create product and send data to rabbitMq
    @PostMapping()
    public ApiResponse<Product> createProduct(@RequestBody Product product) {
        log.info("in test rabbit mq");
        return productService.createProduct(product);
    }

    //import data
    @PostMapping("/create-list")
    public void createProductList(@RequestBody List<Product> products) {
        productService.createListProduct(products);
    }

    @GetMapping("/home")
    public  ResponseEntity<ApiResponse<HomeProductDTO>> Home(){
        return productService.getHomeProduct();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }
}