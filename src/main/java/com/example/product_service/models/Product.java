package com.example.product_service.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String product_id;

    private String url;

    private String name;

    private String description;

    private Double price;

    private String vendor;

    private List<String> images;

    private List<Variant> variants;

    @DBRef
    private List<Category> categories;

    @DBRef
    private List<CharacterEntity> characters;
}
