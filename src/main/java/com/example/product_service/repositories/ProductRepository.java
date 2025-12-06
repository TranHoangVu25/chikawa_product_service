package com.example.product_service.repositories;

import com.example.product_service.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product,String> {
    Page<Product> findAll(Pageable pageable);

    // Lấy 10 sản phẩm mới nhất (sort theo createdAt)
    List<Product> findTop10ByOrderByCreatedAtDesc();

    // Lấy ngẫu nhiên 10 sản phẩm bằng Aggregation
    @Aggregation(pipeline = {
            "{ $sample: { size: 10 } }"
    })
    List<Product> findRandom10Products();
}
