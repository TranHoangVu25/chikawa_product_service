package com.example.product_service.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "product_service")
public class Product {
    @Id
    private String id;

    private String name;

    private String description;

    private Double price;

    private String status;

    private String vendor;

    private List<String> images;

    private List<Variant> variants;

    private List<Category> categories;

    private List<CharacterEntity> characters;
}
