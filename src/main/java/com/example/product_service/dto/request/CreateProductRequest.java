package com.example.product_service.dto.request;

import com.example.product_service.models.Category;
import com.example.product_service.models.CharacterEntity;
import com.example.product_service.models.Variant;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {
    private String product_id;

    private String url;

    private String name;

    private String description;

    private Double price;

    private String vendor;

    private int quantity;

    private List<String> images;

    private List<Variant> variants;

    private List<Category> categories;

    private List<CharacterEntity> characters;
}
