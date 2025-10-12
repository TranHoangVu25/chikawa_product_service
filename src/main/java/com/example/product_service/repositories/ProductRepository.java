package com.example.product_service.repositories;

import com.example.product_service.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends MongoRepository<Product,String> {
}
