package com.example.product_service.controllers;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.dto.response.ApiResponse;
import com.example.product_service.dto.response.PageResponse;
import com.example.product_service.models.Product;
import com.example.product_service.services.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
//    public ApiResponse<List<Product>> getAllProduct(){
//        return productService.findAllProduct();
//    }

    @GetMapping
    public  ApiResponse<PageResponse<Product>> getAllProduct(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ){
        return productService.findAllProduct(page, size, sort);
    }

    @GetMapping("/{productId}")
    public ApiResponse<Product> getProductById(@PathVariable String productId){
        return productService.getProductById(productId);
    }

    @PostMapping()
    public ApiResponse<Product> createProduct(
            @RequestBody CreateProductRequest request
            ){
        return productService.createProduct(request);
    }

    //delete theo product id
    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable String productId){
        return productService.deleteProduct(productId);
    }

    //update theo product Id
    @PutMapping("/{productId}")
    public ApiResponse<Product> updateProduct(@PathVariable String productId,
                                 @RequestBody UpdateProductRequest request
                                 ){
        return productService.updateProduct(productId,request);
    }

    @PostMapping("/test")
    public Product add(@RequestBody Product product) {
        log.info("in test rabiit mq");
        return productService.createProduct_rabbit(product);
    }
}
