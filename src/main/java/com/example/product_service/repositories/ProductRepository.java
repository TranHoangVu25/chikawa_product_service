package com.example.product_service.repositories;

import com.example.product_service.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {
    Page<Product> findAll(Pageable pageable);

}
