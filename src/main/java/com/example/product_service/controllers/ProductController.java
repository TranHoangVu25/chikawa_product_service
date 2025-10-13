package com.example.product_service.controllers;

import com.example.product_service.dto.request.CreateProductRequest;
import com.example.product_service.dto.request.UpdateProductRequest;
import com.example.product_service.models.Product;
import com.example.product_service.services.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    ProductService productService;

    @GetMapping()
    public List<Product> getAllProduct(){
        return productService.findAllProduct();
    }

    @PostMapping()
    public Product createProduct(
            @RequestBody CreateProductRequest request
            ){
        return productService.createProduct(request);
    }
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable String productId){
        productService.deleteProduct(productId);
    }
    @PutMapping("/{productId}")
    public Product updateProduct(@PathVariable String productId,
                                 @RequestBody UpdateProductRequest request
                                 ){
        return productService.updateProduct(productId,request);
    }
}
