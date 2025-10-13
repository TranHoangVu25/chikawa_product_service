package com.example.product_service.repositories;

import com.example.product_service.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product,String> {
    @Query("{ 'product.id' : ?0 }")
    Optional<Product> findByProduct_Id(String productId);

}
